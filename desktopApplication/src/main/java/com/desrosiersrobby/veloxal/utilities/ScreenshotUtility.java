/**
 * ScreenshotUtility.java
 * @Description: contains all code to create, process, save, download and/or upload screenshots
 * Also contains code to get directory from user on save. This class abstracts a lot of the work to provide clearer
 * functions and functionalities to upper level code in WidgetBarController.java class
 */

package com.desrosiersrobby.veloxal.utilities;

import com.desrosiersrobby.veloxal.controllers.ViewfinderController;
import javafx.animation.AnimationTimer;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ScreenshotUtility implements ClipboardOwner {
    private final ViewfinderController viewfinderController;
    FileChooser fileChooser = new FileChooser();
    private String directory;
    private String filetype;
    private final int FRAMES_TO_WAIT = 10;
    private final int THREAD_WAIT_TIME_MS = 100;

    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    private final Map<String, String> imageFileTypes = new HashMap<>(Map.of(
        "bmp (*.bmp)", "*.bmp",
        "png (*.png)", "*.png",
        "jpeg (*.jpeg)", "*.jpeg"
    ));

    public ScreenshotUtility(ViewfinderController viewfinderController) {
        this.viewfinderController = viewfinderController;
    }

    /**
     * Abstracts the code to break down the inputStream gathered from the Flask server and extracts the string
     * the server outputs
     * @param inputStream: the input stream of the requested connection
     * @return String: the text of the input stream
     * @throws IOException
     */
    public String getResponseData(InputStream inputStream) throws IOException {
        // https://stackoverflow.com/questions/10500775/parse-json-from-httpurlconnection-object
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close();
        return sb.toString();
    }

    /**
     * returns the extension of the associated file
     * @param filetype: the filetype passed in from fileChooser
     * @return String
     */
    private String parseExtension(String filetype) {
        return filetype.split(" ")[0];
    }

    /**
     * Opens an instance of the File Chooser class and provides functionality to recieve the directory and file type
     * the user chooses
     * @param fileTypes: The hashmap with different file types of images
     */
    private void getDirectoryFromUser(Map<String, String> fileTypes) {
        fileChooser.getExtensionFilters().clear();
        for (Map.Entry<String,String> fileType : fileTypes.entrySet())
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(fileType.getKey(), fileType.getValue()));

        File selectedFile;
        selectedFile = fileChooser.showSaveDialog(getViewfinder().getStage());
        if (selectedFile != null) {
            directory = selectedFile.getAbsolutePath();
            filetype = parseExtension(fileChooser.getSelectedExtensionFilter().getDescription());
        }
    }

    /**
     * Converts the screen selection of viewfinder to a buffered image used for additional processing
     * @return
     */
    private BufferedImage getScreenshotBufferedImage() {
        BufferedImage screenCapture;
        Rectangle rectangle = new Rectangle(
                (int) getViewfinder().getBoundingBox().getTranslateX(),
                (int) getViewfinder().getBoundingBox().getTranslateY(),
                (int) getViewfinder().getBoundingBox().getWidth(),
                (int) getViewfinder().getBoundingBox().getHeight());
        try {
            screenCapture = new Robot().createScreenCapture(rectangle);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
        return screenCapture;
    }

    /**
     * Used when user clicks the 'Upload Screenshot' widget on the toolbars. This function captures the selected area
     * screen and sends it to the Flask server for processing/saving into the db and file system.
     * This function also copies the link received from the server to the clipboard of the user for quick sharing
     */
    public void uploadScreenshot() {
        getViewfinder().setVisible(false);

        Thread thread = new Thread(() -> {
            HttpURLConnection connection;
            ByteArrayOutputStream baos;

            try {
                // connection setup to flask server
                URL url = new URL(ProgramInfo.getServerIp("/api/v1/upload/"));
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "image/jpeg");
                connection.setRequestMethod("POST");

                // sleeps thread to wait for javaFX Nodes to disappear before capturing screen
                try {
                    Thread.sleep(THREAD_WAIT_TIME_MS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // writing bytes to BAOS
                baos = new ByteArrayOutputStream();
                BufferedImage screenCapture = getScreenshotBufferedImage();
                ImageIO.write(screenCapture, "png", baos);

                // sending bytes to connection
                byte[] bytes = baos.toByteArray();
                baos.write(bytes); // your bytes here
                baos.writeTo(connection.getOutputStream());

                String screenshotURL = getResponseData(connection.getInputStream());
                if (connection.getResponseCode() == 200) {
                    clipboard.setContents(new StringSelection(screenshotURL), new StringSelection(screenshotURL));
                    viewfinderController.getProgramTray().displayMessage("Screenshot URL Copied to Clipboard\n" + screenshotURL, TrayIcon.MessageType.INFO);
                } else {
                    viewfinderController.getProgramTray().displayMessage("Upload Failed: " + connection.getResponseMessage(), TrayIcon.MessageType.WARNING);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                baos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            connection.disconnect();
        });
        thread.start();

        viewfinderController.hideStage();
    }

    /**
     * Saves the captured screen selection to the clipboard of the user to allow paste functionality into programs
     */
    public void saveSingleScreenshotToClipboard() {
        // to ensure none of the components end up in the screenshot
        getViewfinder().setVisible(false);

        // thread handles copying to clipboard/notification of success
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(THREAD_WAIT_TIME_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            clipboard.setContents(new TransferableImage(getScreenshotBufferedImage()), getScreenshotUtility());
            viewfinderController.getProgramTray().displayMessage("Successfully copied screenshot to clipboard", TrayIcon.MessageType.INFO);
        });
        thread.start();
        
        viewfinderController.hideStage();
    }

    /**
     * Saves the selected screen area to a file by allowing user to select the directory and file type to save
     * @throws AWTException
     * @throws IOException
     */
    public void saveSingleScreenshotToFile() throws AWTException, IOException {
        getDirectoryFromUser(imageFileTypes);
        getViewfinder().setVisible(false);

        // if no directory is chosen by user
        if (directory == null) {
            getViewfinder().setVisibilityForScreenshot(true);
            viewfinderController.getProgramTray().displayMessage("No directory chosen. Please choose a valid directory", TrayIcon.MessageType.WARNING);
            return;
        }

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(THREAD_WAIT_TIME_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            File saveFile = new File(directory);
            try {
                ImageIO.write(getScreenshotBufferedImage(), filetype, saveFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                viewfinderController.getProgramTray().displayMessage("Successfully saved screenshot to\n" + directory, TrayIcon.MessageType.INFO);
            }
        });
        thread.start();
        viewfinderController.hideStage();
    }

    public ViewfinderController getViewfinder() {
        return viewfinderController;
    }

    public ScreenshotUtility getScreenshotUtility() {
        return this;
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {

    }
}

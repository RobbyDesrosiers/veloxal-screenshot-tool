/**
 * ScreenshotUtility.java
 * @Description: contains all code to create, process, save, download and/or upload screenshots
 * Also contains code to get directory from user on save. This class abstracts a lot of the work to provide clearer
 * functions and functionalities to upper level code in WidgetBarController.java class
 */

package com.quickshot.quickshot.utilities;

import com.quickshot.quickshot.controllers.ViewfinderController;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ScreenshotUtility implements ClipboardOwner {
    private final ViewfinderController viewfinderController;
    FileChooser fileChooser = new FileChooser();
    private String directory;
    private String filetype;
    private final int FRAMES_TO_WAIT = 35;

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

        AnimationTimer waitForFrameRender = new AnimationTimer() {
            private int frameCount = 0;
            @Override
            public void handle(long timestamp) {
                frameCount++;
                if (frameCount >= FRAMES_TO_WAIT) {
                    stop();

                    HttpURLConnection connection;
                    ByteArrayOutputStream baos;
                    try {
                        // connection setup to flask server
                        URL url = new URL("http://127.0.0.1:5000/api/v1/upload/");
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setDoOutput(true);
                        connection.setRequestProperty("Content-Type", "image/jpeg");
                        connection.setRequestMethod("POST");

                        // writing bytes to BAOS
                        baos = new ByteArrayOutputStream();
                        BufferedImage screenCapture = getScreenshotBufferedImage();
                        ImageIO.write(screenCapture, "png", baos);

                        // sending bytes to connection
                        byte[] bytes = baos.toByteArray();
                        baos.write(bytes); // your bytes here
                        baos.writeTo(connection.getOutputStream());

                        // setting display messages for user
                        viewfinderController.hideStage();

                        String screenshotURL = getResponseData(connection.getInputStream());
                        if (connection.getResponseCode() == 200) {
                            clipboard.setContents(new StringSelection(screenshotURL), new StringSelection(screenshotURL));
                            viewfinderController.getProgramTray().displayMessage("Screenshot URL Copied to Clipboard\n" +
                                    screenshotURL, TrayIcon.MessageType.INFO);
                        } else {
                            viewfinderController.getProgramTray().displayMessage("Upload Failed: " + connection.getResponseMessage(), TrayIcon.MessageType.INFO);
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
                }
            }
        };
        waitForFrameRender.start();
    }

    /**
     * Saves the captured screen selection to the clipboard of the user to allow paste functionality into programs
     */
    public void saveSingleScreenshotToClipboard() {
        // to ensure none of the components end up in the screenshot
        getViewfinder().setVisible(false);

        AnimationTimer waitForFrameRender = new AnimationTimer() {
            private int frameCount = 0;
            @Override
            public void handle(long timestamp) {
                frameCount++;
                if (frameCount >= FRAMES_TO_WAIT) {
                    stop();

                    clipboard.setContents(new TransferableImage(getScreenshotBufferedImage()), getScreenshotUtility());
                    viewfinderController.hideStage();
                    viewfinderController.getProgramTray().displayMessage("Successfully copied screenshot to clipboard", TrayIcon.MessageType.INFO);
                }
            }
        };
        waitForFrameRender.start();
    }

    /**
     * Saves the selected screen area to a file by allowing user to select the directory and file type to save
     * @throws AWTException
     * @throws IOException
     */
    public void saveSingleScreenshotToFile() throws AWTException, IOException {
        // to ensure none of the components end up in the screenshot
        getViewfinder().setVisibilityForScreenshot(false);
        getDirectoryFromUser(imageFileTypes);

        // JavaFX cannot remove the viewfinder quick enough before capturing image.
        // after 25 frames the capture will take place, allowing JavaFX enough time to remove the viewfinder from ScreenOverlay
        // directory chose has an effect on how many passing frames needed before viewfinder removes itself..
        // 10 for src folder, ~25 for desktop. TODO more tests
        // https://stackoverflow.com/questions/39373170/javafx-capture-screen-using-robot
        // https://stackoverflow.com/questions/41287372/how-to-take-snapshot-of-selected-area-of-screen-in-javafx
        AnimationTimer waitForFrameRender = new AnimationTimer() {
            private int frameCount = 0 ;
            @Override
            public void handle(long timestamp) {
                frameCount++ ;
                if (frameCount >= FRAMES_TO_WAIT + 100) { // +100 because save file window doesnt close quick enough
                    stop();

                    // if no directory is chosen by user
                    if (directory == null) {
                        getViewfinder().setVisibilityForScreenshot(true);
                        return; //todo add error window
                    }

                    File saveFile = new File(directory);
                    try {
                        ImageIO.write(getScreenshotBufferedImage(), filetype, saveFile);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    viewfinderController.hideStage();
                    viewfinderController.getProgramTray().displayMessage("Successfully saved screenshot to\n" + directory, TrayIcon.MessageType.INFO);
                }
            }
        };
        waitForFrameRender.start();
    }

    public ViewfinderController getViewfinder() {
        return viewfinderController;
    }

    public ScreenshotUtility getScreenshotUtility() {
        return this;
    }

    /**
     * Needs to be reworked TODO FIX
     */
    public void readTextFromImage() {
        getViewfinder().setVisible(false);

        AnimationTimer waitForFrameRender = new AnimationTimer() {
            private int frameCount = 0;
            @Override
            public void handle(long timestamp) {
                frameCount++;
                if (frameCount >= FRAMES_TO_WAIT) {
                    stop();

                    HttpURLConnection connection;
                    ByteArrayOutputStream baos;
                    try {
                        // connection setup to flask server
                        URL url = new URL("http://127.0.0.1:5000/api/v1/read/");
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setDoOutput(true);
                        connection.setRequestProperty("Content-Type", "image/jpeg");
                        connection.setRequestMethod("POST");

                        // writing bytes to BAOS
                        baos = new ByteArrayOutputStream();
                        BufferedImage screenCapture = getScreenshotBufferedImage();
                        ImageIO.write(screenCapture, "png", baos);

                        // sending bytes to connection
                        byte[] bytes = baos.toByteArray();
                        baos.write(bytes); // your bytes here
                        baos.writeTo(connection.getOutputStream());

                        // setting display messages for user
                        viewfinderController.hideStage();

                        String screenshotURL = getResponseData(connection.getInputStream());
                        if (connection.getResponseCode() == 200) {
                            clipboard.setContents(new StringSelection(screenshotURL), new StringSelection(screenshotURL));
                            viewfinderController.getProgramTray().displayMessage("Text copied to clipboard", TrayIcon.MessageType.INFO);
                        } else {
                            viewfinderController.getProgramTray().displayMessage("Upload Failed: " + connection.getResponseMessage(), TrayIcon.MessageType.INFO);
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
                }
            }
        };
        waitForFrameRender.start();
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {

    }
}

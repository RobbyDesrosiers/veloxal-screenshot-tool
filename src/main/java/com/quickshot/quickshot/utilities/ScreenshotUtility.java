package com.quickshot.quickshot.utilities;

import com.quickshot.quickshot.controllers.ViewfinderController;
import com.squareup.gifencoder.FloydSteinbergDitherer;
import com.squareup.gifencoder.GifEncoder;
import com.squareup.gifencoder.ImageOptions;
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
    private final Map<String, String> gifFileTypes = new HashMap<>(Map.of(
        "gif (*.gif)", "*.gif"
    ));

    public ScreenshotUtility(ViewfinderController viewfinderController) {
        this.viewfinderController = viewfinderController;
    }

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

    private String parseExtension(String filetype) {
        return filetype.split(" ")[0];
    }

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

    private int[][] convertImageToArray(File file) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(file);
        int[][] rgbArray = new int[bufferedImage.getHeight()][bufferedImage.getWidth()];
        for (int i = 0; i < bufferedImage.getHeight(); i++) {
            for (int j = 0; j < bufferedImage.getWidth(); j++) {
                rgbArray[i][j] = bufferedImage.getRGB(j, i);
            }
        }
        return rgbArray;
    }

    private int[][] convertBufferedImageToArray(BufferedImage frame) throws IOException {
        int[][] rgbArray = new int[frame.getHeight()][frame.getWidth()];
        for (int i = 0; i < frame.getHeight(); i++) {
            for (int j = 0; j < frame.getWidth(); j++) {
                rgbArray[i][j] = frame.getRGB(j, i);
            }
        }
        return rgbArray;
    }

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

    public void recordScreen(int fps) {

        // disables movement because for some reason it lags the thread? //todo why
        viewfinderController.allowMovement(false);
        viewfinderController.setVisibilityForScreenshot(false);
        getDirectoryFromUser(gifFileTypes);

        if (directory == null) {
            viewfinderController.allowMovement(true);
            viewfinderController.setVisible(true);
            return; //todo throw error screen
        }

        viewfinderController.allowMousePassthrough(true);
        viewfinderController.setVisible(false);
        Thread recordingThread = new Thread(() -> {
            AnimationTimer waitForFrameRender = new AnimationTimer() {
                final ArrayList<int[][]> capturedScreenArrays = new ArrayList<>();
                final int recordingFPS = 1000 / fps;
                final int maxRecordingLength = 5000; // todo change max value with future testing
                final String fileExtension = "jpg";
                int frameCount = 0;
                int imgSavedCount;
                int[][] frame;
                File saveFile;

                @Override
                public void handle(long timestamp) {
                    frameCount++;
                    if (frameCount >= FRAMES_TO_WAIT) {
                        if (frameCount % recordingFPS == fps) {
                            try {
                                frame = convertBufferedImageToArray(getScreenshotBufferedImage());
                                capturedScreenArrays.add(frame);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            imgSavedCount++;
                        }

                        // todo add keyboard stop here

                        if (frameCount >= maxRecordingLength) {
                            stop();
                            try {
                                convertFrameArraysToGif(fps, capturedScreenArrays, "src/main/java/com/quickshot/quickshot/testImages/video.gif");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            viewfinderController.allowMovement(true);
                            viewfinderController.setVisible(true);
                            viewfinderController.allowMousePassthrough(false);

                        }
                    }
                }
            };
            waitForFrameRender.start();
        });
        recordingThread.setDaemon(true);
        recordingThread.start();
    }

    // found -> https://genuinecoder.com/how-to-create-gif-from-multiple-images-in-java/
    public void convertFrameArraysToGif(int fps, ArrayList<int[][]> arrayOfFrames, String directory) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream("src/main/java/com/quickshot/quickshot/testImages/my_animated_image.gif")) {
            GifEncoder gifEncoder = new GifEncoder(outputStream, (int) getViewfinder().getBoundingBox().getWidth(), (int) getViewfinder().getBoundingBox().getWidth(), 0);
            ImageOptions options = new ImageOptions();

            //Set 500ms between each frame
            options.setDelay(50, TimeUnit.MILLISECONDS);
            //Use Floyd Steinberg dithering as it yields the best quality
            options.setDitherer(FloydSteinbergDitherer.INSTANCE);

            //Create GIF encoder with same dimension as of the source images
            for (int[][] frame : arrayOfFrames) {
                gifEncoder.addImage(frame, options);
            }
            gifEncoder.finishEncoding();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void uploadScreenshot() throws IOException {
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
                        ImageIO.write(screenCapture, "jpg", baos);

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

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {

    }
}

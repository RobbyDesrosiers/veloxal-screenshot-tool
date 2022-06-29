package com.quickshot.quickshot.utilities;

import com.quickshot.quickshot.controllers.ViewfinderController;
import com.squareup.gifencoder.FloydSteinbergDitherer;
import com.squareup.gifencoder.GifEncoder;
import com.squareup.gifencoder.ImageOptions;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ScreenshotUtility implements ClipboardOwner {
    private final ViewfinderController viewfinderController;
    FileChooser fileChooser = new FileChooser();
    private String directory;
    private String filetype;
    private final int FRAMES_TO_WAIT = 35;
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

    private String parseExtension(String filetype) {
        return filetype.split(" ")[0];
    }

    private void getDirectoryFromUser(Map<String, String> fileTypes) {
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

    // found -> https://genuinecoder.com/how-to-create-gif-from-multiple-images-in-java/
    public void convertImagesToGif(int fps) {
        // still testing / working on
        File image1 = new File("C:/Users/Administrator/Desktop/IntelliJProjects/QuickShot/src/main/java/com/quickshot/quickshot/testImages/image1.jpg");
        File image2 = new File("C:/Users/Administrator/Desktop/IntelliJProjects/QuickShot/src/main/java/com/quickshot/quickshot/testImages/image2.jpg");
        File image3 = new File("C:/Users/Administrator/Desktop/IntelliJProjects/QuickShot/src/main/java/com/quickshot/quickshot/testImages/image3.jpg");
        File image4 = new File("C:/Users/Administrator/Desktop/IntelliJProjects/QuickShot/src/main/java/com/quickshot/quickshot/testImages/image4.jpg");

        //The GIF image will be created with file name "my_animated_image.gif"
        try (FileOutputStream outputStream = new FileOutputStream("my_animated_image.gif")) {
            ImageOptions options = new ImageOptions();

            //Set 500ms between each frame
            options.setDelay(500, TimeUnit.MILLISECONDS);
            //Use Floyd Steinberg dithering as it yields the best quality
            options.setDitherer(FloydSteinbergDitherer.INSTANCE);

            //Create GIF encoder with same dimension as of the source images
            new GifEncoder(outputStream, (int) getViewfinder().getBoundingBox().getWidth(), (int) getViewfinder().getBoundingBox().getWidth(), 0)
                    .addImage(convertImageToArray(image1), options)
                    .addImage(convertImageToArray(image2), options)
                    .addImage(convertImageToArray(image3), options)
                    .addImage(convertImageToArray(image4), options)
                    .finishEncoding(); //Start the encoding
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveSingleScreenshotToClipboard() {
        // to ensure none of the components end up in the screenshot
        getViewfinder().setVisibilityForScreenshot(false);

        AnimationTimer waitForFrameRender = new AnimationTimer() {
            private int frameCount = 0;
            @Override
            public void handle(long timestamp) {
                frameCount++;
                if (frameCount >= FRAMES_TO_WAIT) {
                    stop();

                    Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
                    c.setContents(new TransferableImage(getScreenshotBufferedImage()), getScreenshotUtility());
                    Platform.exit();
                }
            }
        };
        waitForFrameRender.start();
    }

    public void saveSingleScreenshotToFile() throws AWTException, IOException {
        // to ensure none of the components end up in the screenshot
        getViewfinder().setVisibilityForScreenshot(false);

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
                if (frameCount >= FRAMES_TO_WAIT) {
                    stop();
                    getDirectoryFromUser(imageFileTypes);

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
                    Platform.exit();
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

package com.quickshot.quickshot;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.stage.DirectoryChooser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ScreenshotUtility {
    private final Viewfinder viewfinder;
    private String directory = "src/main/java/com/quickshot/quickshot/temp"; // todo delete this before prod
    private String fileName; // todo add file name support

    public ScreenshotUtility(Viewfinder viewfinder) {
        this.viewfinder = viewfinder;
    }

    private void getDirectoryFromUser() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory;
        selectedDirectory = directoryChooser.showDialog(getViewfinder().getStage());
        if (selectedDirectory != null) {
            directory = selectedDirectory.getAbsolutePath();
        }
    }

    public void saveToFile() throws AWTException, IOException {
        getDirectoryFromUser();
        if (directory == null) {
            return; //todo add error window
        }
        // to ensure none of the components end up in the screenshot
        getViewfinder().setVisible(false);

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
                if (frameCount >= 25) {
                    stop();

                    // capture process
                    Rectangle rectangle = new Rectangle(
                    (int) getViewfinder().getBoundingBox().getTranslateX(),
                    (int) getViewfinder().getBoundingBox().getTranslateY(),
                    (int) getViewfinder().getBoundingBox().getWidth(),
                    (int) getViewfinder().getBoundingBox().getHeight());

                    try {
                        BufferedImage screenCapture = new Robot().createScreenCapture(rectangle);
                        File f = new File(directory + "/screenCap.bmp");
                        try {
                            ImageIO.write(screenCapture, "bmp", f);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } catch (AWTException e) {
                        throw new RuntimeException(e);
                    }
                    Platform.exit();
                }
            }
        };
        waitForFrameRender.start();
    }

    public Viewfinder getViewfinder() {
        return viewfinder;
    }

}

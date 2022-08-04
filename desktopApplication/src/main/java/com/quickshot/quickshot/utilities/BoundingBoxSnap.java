package com.quickshot.quickshot.utilities;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javafx.scene.input.MouseEvent;
import javafx.util.Pair;

public class BoundingBoxSnap {
    double minLengthForSnap = 200;
    boolean snap;

    ArrayList<Pair<Coordinate, Coordinate>> snappableObjects;

    public BoundingBoxSnap(boolean on) {
        this.snap = on;
        snappableObjects = new ArrayList<>();

    }

    public void checkSnappleObjects() {
        if (snap) {
            Thread thread = new Thread(() -> {
                int r,g,b;
                Color[][] rgbArray = convertBufferedImageToRGB(getScreenAsImage());
                for (int i = 0; i < rgbArray.length; i++) {
                    for (int j = 0; j < rgbArray.length; j++) {
                        r = rgbArray[i][j].getRed();
                        g = rgbArray[i][j].getGreen();
                        b = rgbArray[i][j].getBlue();

//                        System.out.print("(" + r + ", " + g + ", " + b + "), \t\t");
                    }
                }
            });
            thread.start();
        }
    }

    private boolean checkForEdge(Color[] array1, Color[] array2) {
        for (int i = 0; i < array1.length; i++) {
            // write or find algo to find lines
                //https://www.section.io/engineering-education/computer-vision-straight-lines/
            // add to snappableObjects array
            // before user screenshots, send array to boundingBox
                // Inside BoundingBox
                // if mouse gets close ~5px within line, snap boundingBox to line
                // if mouse is +5px away snap back to mouse
        }
        return true;
    }

    private Color[][] convertBufferedImageToRGB(BufferedImage frame) {
        Color[][] rgbArray = new Color[frame.getHeight()][frame.getWidth()];
        for (int i = 0; i < frame.getHeight(); i++) {
            for (int j = 0; j < frame.getWidth(); j++) {
                rgbArray[i][j] = new Color(frame.getRGB(j, i));
            }
        }
        return rgbArray;
    }

    private BufferedImage getScreenAsImage() {
        BufferedImage screenCapture;
        Rectangle rectangle = new Rectangle(
                0,
                0,
                (int) Monitor.getMonitorWidth(),
                (int) Monitor.getMonitorHeight()
        );
        try {
            screenCapture = new Robot().createScreenCapture(rectangle);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
        return screenCapture;
    }

}

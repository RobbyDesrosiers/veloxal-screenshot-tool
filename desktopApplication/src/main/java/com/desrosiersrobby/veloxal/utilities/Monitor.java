/**
 * Monitor.java
 * @Description: Used to hold monitor width/height for easy access
 */

package com.desrosiersrobby.veloxal.utilities;

import java.awt.*;

public class Monitor {
    static final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    static final int monitorWidth = gd.getDisplayMode().getWidth();
    static final int monitorHeight = gd.getDisplayMode().getHeight();

    public static Coordinate getMonitorDimensions() {
        return new Coordinate(monitorWidth, monitorHeight);
    }

    public static double getMonitorWidth() {
        return monitorWidth;
    }

    public static double getMonitorHeight() {
        return monitorHeight;
    }
}

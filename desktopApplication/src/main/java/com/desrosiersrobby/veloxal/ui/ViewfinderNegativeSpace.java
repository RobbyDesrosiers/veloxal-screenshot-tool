/**
 * ViewfinderNegativeSpace.java
 * @Description: Used as a semi-transparent black rectangle which dims portions of the screen outside of the viewfinder
 * purely for graphic/visual effect
 */
package com.desrosiersrobby.veloxal.ui;

import com.desrosiersrobby.veloxal.utilities.DisplayElement;
import com.desrosiersrobby.veloxal.utilities.Monitor;
import javafx.scene.CacheHint;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ViewfinderNegativeSpace extends Rectangle implements DisplayElement {

    public ViewfinderNegativeSpace() {
        setFill(new Color(0, 0, 0, 0.5));
        setCache(true);
        setCacheHint(CacheHint.SPEED);
    }

    public void setFullscreen() {
        setTranslateX(0);
        setTranslateY(0);
        setWidth(Monitor.getMonitorWidth() * 2);
        setHeight(Monitor.getMonitorHeight() * 2);
    }

    @Override
    public void update() {

    }
}

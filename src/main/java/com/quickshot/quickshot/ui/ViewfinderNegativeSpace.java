package com.quickshot.quickshot.ui;

import com.quickshot.quickshot.utilities.DisplayElement;
import javafx.scene.CacheHint;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ViewfinderNegativeSpace extends Rectangle implements DisplayElement {

    public ViewfinderNegativeSpace() {
        setFill(new Color(0, 0, 0, 0.5));
        setCache(true);
        setCacheHint(CacheHint.SPEED);
    }

    @Override
    public void update() {

    }
}

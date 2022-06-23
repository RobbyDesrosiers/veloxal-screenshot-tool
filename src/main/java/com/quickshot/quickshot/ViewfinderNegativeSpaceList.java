package com.quickshot.quickshot;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ViewfinderNegativeSpaceList extends ArrayList<ViewfinderNegativeSpace> implements DisplayElement {
    private ViewfinderBoundingBox boundingBox;
    final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    final int monitorWidth = gd.getDisplayMode().getWidth();
    final int monitorHeight = gd.getDisplayMode().getHeight();
    private ViewfinderNegativeSpace rectLeft = new ViewfinderNegativeSpace();
    private ViewfinderNegativeSpace rectTop = new ViewfinderNegativeSpace();
    private ViewfinderNegativeSpace rectRight = new ViewfinderNegativeSpace();
    private ViewfinderNegativeSpace rectBottom = new ViewfinderNegativeSpace();

    public ViewfinderNegativeSpaceList(ViewfinderBoundingBox boundingBox) {
        this.boundingBox = boundingBox;
        this.addAll(List.of(
                rectLeft,
                rectTop,
                rectRight,
                rectBottom
        ));
    }

    private void calculateRectanglePositions() {
        rectLeft.setTranslateX(0);
        rectLeft.setTranslateY(0);
        rectLeft.setWidth(boundingBox.getMiddleLeft().getX());
        rectLeft.setHeight(monitorHeight * 2);

        rectTop.setTranslateX(boundingBox.getTopLeft().getX());
        rectTop.setTranslateY(0);
        rectTop.setWidth(monitorWidth * 2);
        rectTop.setHeight(boundingBox.getTopMiddle().getY());

        rectRight.setTranslateX(boundingBox.getTopRight().getX());
        rectRight.setTranslateY(boundingBox.getTopRight().getY());
        rectRight.setWidth(monitorWidth * 2);
        rectRight.setHeight(boundingBox.getBottomMiddle().getY() - boundingBox.getTranslateY());

        rectBottom.setTranslateX(boundingBox.getBottomLeft().getX());
        rectBottom.setTranslateY(boundingBox.getBottomLeft().getY());
        rectBottom.setWidth(monitorWidth * 2);
        rectBottom.setHeight(monitorHeight * 2);

    }
    @Override
    public void update() {
        calculateRectanglePositions();
    }
}

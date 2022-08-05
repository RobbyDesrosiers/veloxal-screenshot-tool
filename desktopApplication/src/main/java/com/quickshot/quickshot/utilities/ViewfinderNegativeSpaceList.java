/**
 * ViewfinderNegativeSpaceList.java
 * @Description: Creates a dimmed area around the viewfinder to easily identify the inner/outer potions of the viewfinder
 * Primarily uses ViewfinderNegativeSpace class to create 4 rectangles that position themselves on the sides extending
 * past the screen's width in order to create the dimmed area/visual effect
 */

package com.quickshot.quickshot.utilities;

import com.quickshot.quickshot.ui.ViewfinderBoundingBox;
import com.quickshot.quickshot.ui.ViewfinderNegativeSpace;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class ViewfinderNegativeSpaceList extends ArrayList<ViewfinderNegativeSpace> implements DisplayElement {
    private ViewfinderBoundingBox boundingBox;
    private final ViewfinderNegativeSpace rectLeft;
    private final ViewfinderNegativeSpace rectTop;
    private final ViewfinderNegativeSpace rectRight;
    private final ViewfinderNegativeSpace rectBottom;

    public ViewfinderNegativeSpaceList() {
        rectLeft = new ViewfinderNegativeSpace();
        rectTop = new ViewfinderNegativeSpace();
        rectRight = new ViewfinderNegativeSpace();
        rectBottom = new ViewfinderNegativeSpace();
        this.addAll(List.of(
                rectLeft,
                rectTop,
                rectRight,
                rectBottom
        ));
    }

    public ViewfinderNegativeSpaceList(ViewfinderBoundingBox boundingBox) {
        this();
        setBoundingBox(boundingBox);
    }

    /**
     * Calculates the positions of each of the 4 rectangles in the list
     */
    private void calculateRectanglePositions() {
        rectLeft.setTranslateX(0);
        rectLeft.setTranslateY(0);
        rectLeft.setWidth(boundingBox.getMiddleLeft().getX());
        rectLeft.setHeight(Monitor.getMonitorHeight() * 2);

        rectTop.setTranslateX(boundingBox.getTopLeft().getX());
        rectTop.setTranslateY(0);
        rectTop.setWidth(Monitor.getMonitorWidth() * 2);
        rectTop.setHeight(boundingBox.getTopMiddle().getY());

        rectRight.setTranslateX(boundingBox.getTopRight().getX());
        rectRight.setTranslateY(boundingBox.getTopRight().getY());
        rectRight.setWidth(Monitor.getMonitorWidth() * 2);
        rectRight.setHeight(boundingBox.getBottomMiddle().getY() - boundingBox.getTranslateY());

        rectBottom.setTranslateX(boundingBox.getBottomLeft().getX());
        rectBottom.setTranslateY(boundingBox.getBottomLeft().getY());
        rectBottom.setWidth(Monitor.getMonitorWidth() * 2);
        rectBottom.setHeight(Monitor.getMonitorHeight() * 2);

    }
    @Override
    public void update() {
        calculateRectanglePositions();
    }

    public void setVisible(boolean b) {
        for (ViewfinderNegativeSpace rect : this) {
            rect.setVisible(b);
        }
    }

    public void setBoundingBox(ViewfinderBoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    /**
     * Sets each rectangle (NegativeSpace) to handle the mouseHandler passed in
     * @see com.quickshot.quickshot.controllers.UserController for where its used
     * @param mouseHandler: The function that is being handled by UserController
     */
    public void setOnMouseDragged(EventHandler<? super MouseEvent> mouseHandler) {
        for (ViewfinderNegativeSpace rect : this) {
            rect.setOnMouseDragged(mouseHandler);
        }
    }

    /**
     * Sets each rectangle (NegativeSpace) to handle the mouseHandler passed in
     * @see com.quickshot.quickshot.controllers.UserController for where its used
     * @param mouseHandler: The function that is being handled by UserController
     */
    public void setOnMousePressed(EventHandler<? super MouseEvent> mouseHandler) {
        for (ViewfinderNegativeSpace rect : this) {
            rect.setOnMousePressed(mouseHandler);
        }
    }

    /**
     * Sets each rectangle (NegativeSpace) to handle the mouseHandler passed in
     * @see com.quickshot.quickshot.controllers.UserController for where its used
     * @param mouseHandler: The function that is being handled by UserController
     */
    public void setOnMouseReleased(EventHandler<? super MouseEvent> mouseHandler) {
        for (ViewfinderNegativeSpace rect : this) {
            rect.setOnMouseReleased(mouseHandler);
        }
    }
}

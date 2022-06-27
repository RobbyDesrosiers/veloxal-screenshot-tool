package com.quickshot.quickshot;

import javafx.application.Platform;
import javafx.geometry.Orientation;
import java.util.LinkedList;

public class WidgetBarDrawingTools extends WidgetBar implements DisplayElement {
    private final WidgetUndo undoButton;
    private final WidgetClose closeButton;

    public WidgetBarDrawingTools(ViewfinderBoundingBox boundingBox, WidgetDrawData drawData) {
        super(boundingBox, drawData);
        setOrientation(Orientation.HORIZONTAL);

        // create widgets
        addWidget(new WidgetPaintbrush("paint-brush.png", drawData));
        addWidget(new WidgetHighlighter("marker.png", drawData));
        addWidget(new WidgetRectangle("rectangle.png", drawData));
        addWidget(new WidgetText("text.png", drawData));
        undoButton = new WidgetUndo("undo.png", drawData);
        closeButton = new WidgetClose("close.png");
        getToolBar().getItems().add(undoButton);
        getToolBar().getItems().add(closeButton);

        initMouseEvents();
    }

    private void initMouseEvents() {
        // undo button is a 1 press event, does not need to be selected. This function ensures the currently selected
        // widget stays selected even when pressing the undo button
        getUndoButton().setOnMouseReleased(e -> handleUndoButton());
        getCloseButton().setOnMouseClicked(e -> Platform.exit());
    }

    private void handleUndoButton() {
        Widget selectedWidget = getSelectedWidget();
        // moves the latest permanent drawn data into a deleted data list which will be removed from within
        // UserController -> refreshScreen() function
        if (getDrawData().size() > 0)
            getDrawData().getDeletedData().push(new LinkedList<>(getDrawData().pop()));
        if (selectedWidget != null)
            selectedWidget.setSelected(true);

        undoButton.handleButtonPress();
    }

    public void calculateScreenPosition() {
        int OFFSET = 10;
        double Y_PADDING;
        double X_PADDING;

        if (getBoundingBox().getBottomMiddle().getY() + getHeight() + OFFSET > Monitor.getMonitorHeight()) {
            Y_PADDING = -getHeight() - 5;
            X_PADDING = -5;
        } else {
            Y_PADDING = 5;
            X_PADDING = 0;
        }

        setTranslateX(getBoundingBox().getBottomRight().getX() - getWidth() + X_PADDING);
        setTranslateY(getBoundingBox().getBottomMiddle().getY() + Y_PADDING);
        setViewOrder(-1);
    }

    public void setWidgetDrawingStatus(boolean b) {
        for (Widget widget : getWidgets()) {
            widget.setDrawing(b);
        }
    }

    public boolean isWidgetDrawing() {
        for (Widget widget : getWidgets()) {
            if (widget.isDrawing()) {
                return true;
            }
        }
        return false;
    }

    public WidgetUndo getUndoButton() {
        return undoButton;
    }

    public WidgetClose getCloseButton() {
        return closeButton;
    }

    @Override
    public void update() {
        calculateScreenPosition();
    }
}

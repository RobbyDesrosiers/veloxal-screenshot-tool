package com.quickshot.quickshot.ui;

import com.quickshot.quickshot.ui.abstracts.WidgetBar;
import com.quickshot.quickshot.utilities.DisplayElement;
import com.quickshot.quickshot.utilities.Monitor;
import com.quickshot.quickshot.utilities.WidgetDrawData;
import javafx.geometry.Orientation;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

public class WidgetBarDrawingTools extends WidgetBar implements DisplayElement {
    private final WidgetUndo undoButton;
    public WidgetBarDrawingTools(ViewfinderBoundingBox boundingBox, WidgetDrawData drawData) {
        super(boundingBox, drawData);
        setOrientation(Orientation.HORIZONTAL);

        // create widgets
        addToggleWidget(new WidgetPaintbrush("paint-brush.png", drawData));
        addToggleWidget(new WidgetHighlighter("marker.png", drawData));
        addToggleWidget(new WidgetRectangle("rectangle.png", drawData));
        addToggleWidget(new WidgetText("text.png", drawData));
        undoButton = new WidgetUndo("undo.png", drawData);
        undoButton.setClickableOnly();
        getToolBar().getItems().add(undoButton);
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
        setViewOrder(-1); // todo fix this
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

    @Override
    public void update() {
        calculateScreenPosition();
    }
}

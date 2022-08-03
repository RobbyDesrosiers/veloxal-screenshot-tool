package com.quickshot.quickshot.ui;

import com.quickshot.quickshot.ui.abstracts.WidgetBar;
import com.quickshot.quickshot.utilities.DisplayElement;
import com.quickshot.quickshot.utilities.Monitor;
import com.quickshot.quickshot.utilities.ViewfinderViewOrder;
import com.quickshot.quickshot.utilities.WidgetDrawData;
import javafx.geometry.Pos;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

public class WidgetBarDrawingTools extends WidgetBar implements DisplayElement {
    private final Widget undoButton;

    public WidgetBarDrawingTools(ViewfinderBoundingBox boundingBox, WidgetDrawData drawData) {
        super(boundingBox, drawData);
        setHgap(2);
        setPrefWidth(265);
        setAlignment(Pos.CENTER_RIGHT);

        // create widgets
        ColorPicker colorPicker = new ColorPicker(Color.RED);
        colorPicker.setPrefWidth(30);
        colorPicker.setPrefHeight(24);

        addToggleWidget(new WidgetPaintbrush("paint-brush.png", drawData, colorPicker));
        addToggleWidget(new WidgetHighlighter("marker.png", drawData, colorPicker));
        addToggleWidget(new WidgetRectangle("rectangle.png", drawData, colorPicker));
        addToggleWidget(new WidgetArrow("arrow.png", drawData, colorPicker));
        addToggleWidget(new WidgetLine("line.png", drawData, colorPicker));
        addToggleWidget(new WidgetText("text.png", drawData, colorPicker));
        undoButton = new Widget("undo.png");
        undoButton.setClickableOnly();
        addWidget(undoButton);
        getChildren().add(colorPicker);
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
        setViewOrder(ViewfinderViewOrder.WIDGET_BAR);
    }

    public void setWidgetsDrawingStatus(boolean b) {
        for (Widget widget : getWidgets()) {
            widget.setDrawing(b);
        }
    }

    public void setWidgetsSelected(boolean b) {
        for (Widget widget : getWidgets()) {
            widget.setSelected(b);
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

    public Widget getUndoButton() {
        return undoButton;
    }

    @Override
    public void update() {
        calculateScreenPosition();
    }
}

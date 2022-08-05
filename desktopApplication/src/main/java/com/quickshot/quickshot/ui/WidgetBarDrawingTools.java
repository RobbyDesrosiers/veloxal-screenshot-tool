/**
 * WidgetBarDrawingTools.java
 * @Description: This is the graphical element and controller of the widget bar located on the bottom of the
 * viewfinder. This holds all the drawing tools which are toggleable and allow the user to draw onto the screen
 */
package com.quickshot.quickshot.ui;

import com.quickshot.quickshot.ui.abstracts.WidgetBar;
import com.quickshot.quickshot.utilities.DisplayElement;
import com.quickshot.quickshot.utilities.Monitor;
import com.quickshot.quickshot.utilities.ViewfinderViewOrder;
import com.quickshot.quickshot.utilities.WidgetDrawData;
import javafx.geometry.Pos;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;

public class WidgetBarDrawingTools extends WidgetBar implements DisplayElement {
    private final Widget undoButton;
    private final WidgetBarStrokeWidth widgetBarStrokeWidth;

    public WidgetBarDrawingTools(ViewfinderBoundingBox boundingBox, WidgetDrawData drawData) {
        super(boundingBox, drawData);
        setHgap(2);
        setPrefWidth(265);
        setAlignment(Pos.CENTER_RIGHT);

        // creates all the widgets and adds them to the widget bar
        ColorPicker colorPicker = new ColorPicker(Color.RED);
        colorPicker.getStyleClass().add("button");
        colorPicker.setTooltip(new Tooltip("Select Paint"));
        colorPicker.setPrefWidth(30);
        colorPicker.setPrefHeight(24);

        // creates the stroke width changer
        widgetBarStrokeWidth = new WidgetBarStrokeWidth();

        addToggleWidget(new WidgetPaintbrush("brush", drawData, colorPicker, widgetBarStrokeWidth));
        addToggleWidget(new WidgetHighlighter("highlighter", drawData, colorPicker, widgetBarStrokeWidth));
        addToggleWidget(new WidgetRectangle("rect", drawData, colorPicker, widgetBarStrokeWidth));
        addToggleWidget(new WidgetArrow("arrow", drawData, colorPicker, widgetBarStrokeWidth));
        addToggleWidget(new WidgetLine("line", drawData, colorPicker, widgetBarStrokeWidth));
        addToggleWidget(new WidgetText("text", drawData, colorPicker, widgetBarStrokeWidth));
        undoButton = new Widget("back", "Undo Line");
        undoButton.setClickableOnly();
        addWidget(undoButton);
        getChildren().add(colorPicker);
    }

    /**
     * Calculates the screen position of the widget bar
     */
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
        widgetBarStrokeWidth.setTranslateX(getBoundingBox().getBottomRight().getX() - getWidth() + X_PADDING);
        setTranslateY(getBoundingBox().getBottomMiddle().getY() + Y_PADDING);
        widgetBarStrokeWidth.setTranslateY(getBoundingBox().getBottomMiddle().getY() + Y_PADDING + getHeight() + 5);
        setViewOrder(ViewfinderViewOrder.WIDGET_BAR);
    }

    /**
     * Sets all of the widgets' drawing status to the boolean value (typically false when used) in order to
     * ensure all widgets' drawing status variable is false when user is not drawing
     * @param b boolean
     */
    public void setWidgetsDrawingStatus(boolean b) {
        for (Widget widget : getWidgets()) {
            widget.setDrawing(b);
        }
    }

    /**
     * Sets the selected status of all widgets to the boolean value
     * @param b
     */
    public void setWidgetsSelected(boolean b) {
        for (Widget widget : getWidgets()) {
            widget.setSelected(b);
        }
    }

    /**
     * returns the boolean value when checking if any of the widgets' drawing status is set
     * @return
     */
    public boolean isWidgetDrawing() {
        for (Widget widget : getWidgets()) {
            if (widget.isDrawing()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a drawing widget is selected and if so will set the stroke adjuster to visible
     */
    private void checkIfBrushStrokeIsNeeded() {
        widgetBarStrokeWidth.setVisible(isWidgetSelected());
    }

    public WidgetBarStrokeWidth getWidgetBarStrokeWidth() {
        return widgetBarStrokeWidth;
    }

    public Widget getUndoButton() {
        return undoButton;
    }

    @Override
    public void update() {
        calculateScreenPosition();
        checkIfBrushStrokeIsNeeded();
    }
}

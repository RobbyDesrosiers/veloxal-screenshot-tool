/**
 * WidgetLine.java
 * @Description: Adds a line to the screen
 */

package com.quickshot.quickshot.ui;

import com.quickshot.quickshot.ui.abstracts.WidgetLineDrawer;
import com.quickshot.quickshot.utilities.ViewfinderViewOrder;
import com.quickshot.quickshot.utilities.WidgetDrawData;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class WidgetLine extends WidgetLineDrawer {
    private Line arrowBodyLine;

    public WidgetLine(String fileName, WidgetDrawData drawData, ColorPicker colorPicker, WidgetBarStrokeWidth widgetBarStrokeWidth) {
        super(fileName, drawData, colorPicker, widgetBarStrokeWidth);
    }

    @Override
    public void draw(MouseEvent mouseEvent) {
        setFillColor(getColorPicker().getValue());

        if (getDrawData().getTempData().size() == 0 || getDrawData().isNewLine()) {
            addShape(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            getDrawData().setNewLine(false);
        } else {
            double endX = mouseEvent.getSceneX();
            double endY = mouseEvent.getSceneY();
            arrowBodyLine.setEndX(endX);
            arrowBodyLine.setEndY(endY);
        }
    }

    @Override
    public void addShape(double x, double y) {
        arrowBodyLine = new Line(x,y,x,y);
        arrowBodyLine.setViewOrder(ViewfinderViewOrder.WIDGET_DRAWING);
        arrowBodyLine.setStroke(getFillColor());
        arrowBodyLine.setStrokeWidth(getStrokeWidth());
        pushShape(arrowBodyLine);
    }

}

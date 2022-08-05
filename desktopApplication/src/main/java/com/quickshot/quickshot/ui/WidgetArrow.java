/**
 * WidgetArrow.java
 * @Description: Draws an arrow onto the screen
 */

package com.quickshot.quickshot.ui;

import com.quickshot.quickshot.ui.abstracts.WidgetLineDrawer;
import com.quickshot.quickshot.utilities.ViewfinderViewOrder;
import com.quickshot.quickshot.utilities.WidgetDrawData;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class WidgetArrow extends WidgetLineDrawer {
    private Line arrowBodyLine;
    private Polygon arrowHead;

    public WidgetArrow(String fileName, WidgetDrawData drawData, ColorPicker colorPicker, WidgetBarStrokeWidth widgetBarStrokeWidth) {
        super(fileName, drawData, colorPicker, widgetBarStrokeWidth);
    }

    @Override
    public void draw(MouseEvent mouseEvent) {
        setFillColor(getColorPicker().getValue());

        // checks if this is a newly drawn arrow, if not it will modify the arrow's position when user drags around
        // the screen
        if (getDrawData().getTempData().size() == 0 || getDrawData().isNewLine()) {
            addShape(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            getDrawData().setNewLine(false);
        } else {
            double endX = mouseEvent.getSceneX();
            double endY = mouseEvent.getSceneY();
            double startX = arrowBodyLine.getStartX();
            double startY = arrowBodyLine.getStartY();
            double defaultArrowHeadSize = 12.0;

            arrowBodyLine.setEndX(endX);
            arrowBodyLine.setEndY(endY);

            //ArrowHead
            double angle = Math.atan2((endY - startY), (endX - startX)) - Math.PI / 2.0;
            double sin = Math.sin(angle);
            double cos = Math.cos(angle);
            //point1
            double x1 = (- 1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * defaultArrowHeadSize + endX;
            double y1 = (- 1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * defaultArrowHeadSize + endY;
            //point2
            double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * defaultArrowHeadSize + endX;
            double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * defaultArrowHeadSize + endY;

            arrowHead.getPoints().setAll(
                    endX, endY,
                    x1,y1,
                    x2,y2
            );
        }
    }

    /**
     * Creates a newly drawn arrow shape
     * @param x: location of the x coordinate of the mouse
     * @param y: location of the y coordinate of the mouse
     */
    @Override
    public void addShape(double x, double y) {
        arrowBodyLine = new Line(x,y,x,y);
        arrowHead = new Polygon();
        arrowHead.getPoints().addAll(
                0.0, 0.0,
                0.0, 0.0,
                0.0, 0.0);

        arrowBodyLine.setViewOrder(ViewfinderViewOrder.WIDGET_DRAWING);
        arrowBodyLine.setStroke(getFillColor());
        arrowHead.setViewOrder(ViewfinderViewOrder.WIDGET_DRAWING);
        arrowHead.setFill(getFillColor());

        pushShape(arrowHead);
        pushShape(arrowBodyLine);
    }

}

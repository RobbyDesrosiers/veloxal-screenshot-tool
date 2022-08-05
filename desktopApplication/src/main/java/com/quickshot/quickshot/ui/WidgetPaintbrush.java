/**
 * WidgetPaintBrush.java
 * @Description: Draws small circles onto the screen in order to visually represent a dynamic line drawn by the user
 */
package com.quickshot.quickshot.ui;

import com.quickshot.quickshot.ui.abstracts.WidgetLineDrawer;
import com.quickshot.quickshot.utilities.ViewfinderViewOrder;
import com.quickshot.quickshot.utilities.WidgetDrawData;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class WidgetPaintbrush extends WidgetLineDrawer {

    public WidgetPaintbrush(String fileName, WidgetDrawData drawData, ColorPicker colorPicker, WidgetBarStrokeWidth widgetBarStrokeWidth) {
        super(fileName, drawData, colorPicker, widgetBarStrokeWidth);
    }

    @Override
    public void draw(MouseEvent mouseEvent) {
        setFillColor(getColorPicker().getValue());
        if (getDrawData().getTempData().size() == 0 || getDrawData().isNewLine()) {
            addShape(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            getDrawData().setNewLine(false);
        } else {
            addShape(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            // grabs 2 top nodes
            Node f = getDrawData().getTempData().pop();
            Node f2 = getDrawData().getTempData().peek();
            // pushes popped node back onto stack
            getDrawData().getTempData().push(f);
            correctLines(f2.getTranslateX(), f2.getTranslateY(), f.getTranslateX(), f.getTranslateY());
        }
    }

    @Override
    public void addShape(double x, double y) {
        Circle c = new Circle(getStrokeWidth());
        c.setViewOrder(1);
        c.setFill(getFillColor());
        c.setTranslateX(x);
        c.setTranslateY(y);
        c.setViewOrder(ViewfinderViewOrder.WIDGET_DRAWING);
        pushShape(c);
    }
}

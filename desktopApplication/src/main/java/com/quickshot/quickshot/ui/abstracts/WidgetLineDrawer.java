/**
 * WidgetLineDrawer.java
 * @Description: Extends the base Widget class functionality to allow easy additional drawing functionality for
 * any widgets which draw objects onto the screen via the mouse
 */

package com.quickshot.quickshot.ui.abstracts;

import com.quickshot.quickshot.ui.Widget;
import com.quickshot.quickshot.utilities.WidgetDrawData;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

public abstract class WidgetLineDrawer extends Widget {
    private Color fillColor;
    private WidgetDrawData drawData;
    private ColorPicker colorPicker;

    public WidgetLineDrawer(String fileName, WidgetDrawData drawData, ColorPicker colorPicker) {
        super(fileName);
        this.drawData = drawData;
        this.colorPicker = colorPicker;
        setFillColor(colorPicker.getValue());
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public WidgetDrawData getDrawData() {
        return drawData;
    }

    public void setDrawData(WidgetDrawData drawData) {
        this.drawData = drawData;
    }

    public ColorPicker getColorPicker() {
        return colorPicker;
    }

    public void setColorPicker(ColorPicker colorPicker) {
        this.colorPicker = colorPicker;
    }

    public void pushShape(Node node) {
        getDrawData().getTempData().push(node);
    }

    /**
     * Because of lag between mouse movements, sometimes we must connect two points of drawn objects and draw a new
     * line between those two points. This algorithm does that
     * @param x0: point 1 x coord
     * @param y0: point 1 y coord
     * @param x1: point 2 x coord
     * @param y1: point 2 y coord
     */
    public void correctLines(double x0, double y0, double x1, double y1) {
        double dx = Math.abs(x1 - x0);
        double dy = Math.abs(y1 - y0);
        double sx = x0 < x1 ? 1 : -1;
        double sy = y0 < y1 ? 1 : -1;
        double err = dx - dy;
        double e2;

        while (true) {
            addShape(x0, y0);
            if (x0 == x1 && y0 == y1)
                break;

            e2 = 2 * err;
            if (e2 > -dy) {
                err = err - dy;
                x0 = x0 + sx;
            }
            if (e2 < dx) {
                err = err + dx;
                y0 = y0 + sy;
            }
        }
    }

    public void addShape(double x, double y) {

    }
}

package com.quickshot.quickshot;

import javafx.scene.Node;
import javafx.scene.paint.Color;

public abstract class WidgetLineDrawer extends Widget {
    private Color brushColor;
    private WidgetDrawData drawData;

    public WidgetLineDrawer(String fileName, WidgetDrawData drawData) {
        super(fileName);
        this.drawData = drawData;
    }

    public Color getBrushColor() {
        return brushColor;
    }

    public void setBrushColor(Color brushColor) {
        this.brushColor = brushColor;
    }

    public WidgetDrawData getDrawData() {
        return drawData;
    }

    public void setDrawData(WidgetDrawData drawData) {
        this.drawData = drawData;
    }

    public void pushShape(Node node) {
        getDrawData().push(node);
    }

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

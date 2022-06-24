package com.quickshot.quickshot;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.LinkedList;

public class WidgetPaintbrush extends Widget {
    private int brushSize = 1;
    private Color paintBrushColor = Color.RED;
    private LinkedList<Node> drawData;

    public WidgetPaintbrush(String fileName, WidgetDrawData drawData) {
        super(fileName);
        this.drawData = drawData;
    }

    @Override
    public void draw(MouseEvent mouseEvent) {
        if (drawData.size() == 0) {
            addCircle(mouseEvent.getSceneX(), mouseEvent.getSceneY());
        } else {
            addCircle(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            // grabs 2 top nodes
            Node f = drawData.pop();
            Node f2 = drawData.peek();
            // pushes popped node back onto stack
            drawData.push(f);
            correctLines(f2.getTranslateX(), f2.getTranslateY(), f.getTranslateX(), f.getTranslateY());
        }
    }

    public void correctLines(double x0, double y0, double x1, double y1) {
        double dx = Math.abs(x1 - x0);
        double dy = Math.abs(y1 - y0);
        double sx = x0 < x1 ? 1 : -1;
        double sy = y0 < y1 ? 1 : -1;
        double err = dx - dy;
        double e2;

        while (true) {
            addCircle(x0, y0);
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

    public void addCircle(double x, double y) {
        Circle c = new Circle(brushSize);
        c.toBack();
        c.setFill(paintBrushColor);
        c.setTranslateX(x);
        c.setTranslateY(y);
        drawData.push(c);
    }
}

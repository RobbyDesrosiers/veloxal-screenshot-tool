package com.quickshot.quickshot;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class WidgetPaintbrush extends WidgetLineDrawer {
    private int brushSize = 1;

    public WidgetPaintbrush(String fileName, WidgetDrawData drawData) {
        super(fileName, drawData);
        setBrushColor(Color.RED);
    }

    @Override
    public void draw(MouseEvent mouseEvent) {
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
        Circle c = new Circle(getBrushSize());
        c.setViewOrder(1);
        c.setFill(getBrushColor());
        c.setTranslateX(x);
        c.setTranslateY(y);
        pushShape(c);
    }

    public int getBrushSize() {
        return brushSize;
    }

    public void setBrushSize(int brushSize) {
        this.brushSize = brushSize;
    }
}

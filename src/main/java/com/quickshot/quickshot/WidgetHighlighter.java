package com.quickshot.quickshot;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class WidgetHighlighter extends WidgetLineDrawer {
    private int brushSize = 1;

    public WidgetHighlighter(String fileName, WidgetDrawData drawData) {
        super(fileName, drawData);
        setBrushColor(new Color(1,1,0,1));
    }

    @Override
    public void draw(MouseEvent mouseEvent) {
        if (getDrawData().size() == 0 || getDrawData().isNewLine()) {
            addShape(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            getDrawData().setNewLine(false);
        } else {
            addShape(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            // grabs 2 top nodes
            Node f = getDrawData().pop();
            Node f2 = getDrawData().peek();
            // pushes popped node back onto stack
            getDrawData().push(f);
            correctLines(f2.getTranslateX(), f2.getTranslateY(), f.getTranslateX(), f.getTranslateY());
        }
    }

    @Override
    public void addShape(double x, double y) {
        int brushHeight = 10;
        Rectangle c = new Rectangle(getBrushSize(), brushHeight);
        c.toBack();
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

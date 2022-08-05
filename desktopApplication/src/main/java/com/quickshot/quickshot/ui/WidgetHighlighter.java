/**
 * WidgetHighlighter.java
 * @Description: draws a thick vertical line in order to easy black out sensitive text
 * Will need to eventually add/fix the transparency issue to allow it to function as an actual highlighter
 */
package com.quickshot.quickshot.ui;

import com.quickshot.quickshot.ui.abstracts.WidgetLineDrawer;
import com.quickshot.quickshot.utilities.ViewfinderViewOrder;
import com.quickshot.quickshot.utilities.WidgetDrawData;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

import java.util.Objects;


public class WidgetHighlighter extends WidgetLineDrawer {
    private int brushSize = 1;
    private Double mouseTranslateY = null;

    public WidgetHighlighter(String fileName, WidgetDrawData drawData, ColorPicker colorPicker) {
        super(fileName, drawData, colorPicker);
    }

    @Override
    public void draw(MouseEvent mouseEvent) {
        setFillColor(getColorPicker().getValue());
        // used for horizontal shift functionality
        if (!mouseEvent.isShiftDown() || getDrawData().isNewLine())
            mouseTranslateY = null;
        // used for horizontal shift functionality
        if (mouseEvent.isShiftDown() && mouseTranslateY == null)
            mouseTranslateY = mouseEvent.getSceneY();

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
        int brushHeight = 15;
        Rectangle c = new Rectangle(getBrushSize(), brushHeight);
        c.toBack();
        c.setFill(getFillColor());
        c.setTranslateX(x);
        c.setViewOrder(ViewfinderViewOrder.WIDGET_DRAWING);

        // if null use Y, or else use mouseTranslateY
        c.setTranslateY(Objects.requireNonNullElse(mouseTranslateY, y));

        pushShape(c);
    }

    public int getBrushSize() {
        return brushSize;
    }

    public void setBrushSize(int brushSize) {
        this.brushSize = brushSize;
    }
}

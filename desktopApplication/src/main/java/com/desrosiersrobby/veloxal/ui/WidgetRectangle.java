/**
 * WidgetRectangle.java
 * @Description: Widget that allows the user to draw a rectangle with an outline onto the screen
 */
package com.desrosiersrobby.veloxal.ui;

import com.desrosiersrobby.veloxal.ui.abstracts.WidgetLineDrawer;
import com.desrosiersrobby.veloxal.utilities.Coordinate;
import com.desrosiersrobby.veloxal.utilities.ViewfinderViewOrder;
import com.desrosiersrobby.veloxal.utilities.WidgetDrawData;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class WidgetRectangle extends WidgetLineDrawer {
    private Rectangle rect;
    private final Coordinate topLeftAnchorRectPosition;

    public WidgetRectangle(String fileName, WidgetDrawData drawData, ColorPicker colorPicker, WidgetBarStrokeWidth widgetBarStrokeWidth) {
        super(fileName, drawData, colorPicker, widgetBarStrokeWidth);
        topLeftAnchorRectPosition = new Coordinate();
    }

    @Override
    public void draw(MouseEvent mouseEvent) {
        setFillColor(getColorPicker().getValue());
        if (getDrawData().getTempData().size() == 0 || getDrawData().isNewLine()) {
            addShape(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            getDrawData().setNewLine(false);
        } else {
            drawRectangle(mouseEvent);
        }
    }

    @Override
    public void addShape(double x, double y) {
        rect = new Rectangle(x, y);
        rect.setViewOrder(1);
        rect.setFill(Color.TRANSPARENT);
        rect.setStroke(getFillColor());
        rect.setStrokeWidth(getStrokeWidth());
        rect.setTranslateX(x);
        rect.setWidth(0);
        rect.setHeight(0);
        topLeftAnchorRectPosition.setX(x);
        topLeftAnchorRectPosition.setY(y);
        rect.setTranslateY(y);
        rect.setViewOrder(ViewfinderViewOrder.WIDGET_DRAWING);
        pushShape(rect);
    }

    private void drawRectangle(MouseEvent mouseEvent) {
        boolean xCross = mouseEvent.getSceneX() < topLeftAnchorRectPosition.getX();
        boolean yCross = mouseEvent.getSceneY() < topLeftAnchorRectPosition.getY();

        if (xCross && yCross) {
            moveLeft(mouseEvent);
            moveUp(mouseEvent);
            return;
        }

        if (xCross) {
            moveLeft(mouseEvent);
            moveDown(mouseEvent);
        }
        if (yCross) {
            moveUp(mouseEvent);
            moveRight(mouseEvent);
        }

        if (!xCross && !yCross) {
            moveRight(mouseEvent);
            moveDown(mouseEvent);
        }
    }

    private void moveRight(MouseEvent mouseEvent) {
        rect.setWidth(mouseEvent.getSceneX() - rect.getTranslateX());
    }

    private void moveDown(MouseEvent mouseEvent) {
        rect.setHeight(mouseEvent.getSceneY() - rect.getTranslateY());
    }

    private void moveUp(MouseEvent mouseEvent) {
        rect.setTranslateY(mouseEvent.getSceneY());
        rect.setHeight(topLeftAnchorRectPosition.getY() - rect.getTranslateY());
    }

    private void moveLeft(MouseEvent mouseEvent) {
        rect.setTranslateX(mouseEvent.getSceneX());
        rect.setWidth(topLeftAnchorRectPosition.getX() - rect.getTranslateX());
    }

}

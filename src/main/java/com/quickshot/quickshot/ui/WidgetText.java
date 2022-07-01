package com.quickshot.quickshot.ui;

import com.quickshot.quickshot.ui.abstracts.WidgetLineDrawer;
import com.quickshot.quickshot.utilities.Coordinate;
import com.quickshot.quickshot.utilities.ViewfinderViewOrder;
import com.quickshot.quickshot.utilities.WidgetDrawData;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.awt.*;

public class WidgetText extends WidgetLineDrawer {
    TextArea text;
    private final Coordinate topLeftAnchorRectPosition;
    private double currentTextAreaHeight;

    public WidgetText(String fileName, WidgetDrawData drawData, ColorPicker colorPicker) {
        super(fileName, drawData, colorPicker);
        setIconSize(13);
        topLeftAnchorRectPosition = new Coordinate();
    }

    @Override
    public void draw(MouseEvent mouseEvent) {
        setBrushColor(getColorPicker().getValue());
        if (getDrawData().getTempData().size() == 0 || getDrawData().isNewLine()) {
            addShape(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            getDrawData().setNewLine(false);
        } else {
            drawRectangle(mouseEvent);
        }
    }

    @Override
    public void addShape(double x, double y) {
        text = new TextArea();

        // adds height on new line creation so textarea does not auto-scroll vertically
        text.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            int NEW_LINE_PX_SIZE = 17;
            int NEW_LINE_COUNT = text.getText().split("\n").length;
            KeyCode keyPressed = e.getCode();
            if (keyPressed == KeyCode.ENTER) {
                text.setPrefHeight(currentTextAreaHeight + (NEW_LINE_PX_SIZE * NEW_LINE_COUNT));
            }
        });

        text.setViewOrder(ViewfinderViewOrder.WIDGET_TEXT);
        text.setTranslateX(x);
        text.setTranslateY(y);
        pushShape(text);
        text.setPrefWidth(0);
        text.setPrefHeight(0);
        text.setMinWidth(30);
        text.setMinHeight(30);
        text.setWrapText(true);
        topLeftAnchorRectPosition.setX(x);
        topLeftAnchorRectPosition.setY(y);
        text.setTranslateY(y);
        pushShape(text);
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

        currentTextAreaHeight = text.getHeight();
    }

    private void moveRight(MouseEvent mouseEvent) {
        text.setPrefWidth(mouseEvent.getSceneX() - text.getTranslateX());
    }

    private void moveDown(MouseEvent mouseEvent) {
        text.setPrefHeight(mouseEvent.getSceneY() - text.getTranslateY());
    }

    private void moveUp(MouseEvent mouseEvent) {
        text.setTranslateY(mouseEvent.getSceneY());
        text.setPrefHeight(topLeftAnchorRectPosition.getY() - text.getTranslateY());
    }

    private void moveLeft(MouseEvent mouseEvent) {
        text.setTranslateX(mouseEvent.getSceneX());
        text.setPrefWidth(topLeftAnchorRectPosition.getX() - text.getTranslateX());
    }

}

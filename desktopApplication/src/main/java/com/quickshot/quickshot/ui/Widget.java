/**
 * Widget.java
 * @Description: Widget is a base class that is preferred to be inherited from, although it can be used as is.
 * This class functions as a base to what is needed for the Widgets that form the toolbars.
 */

package com.quickshot.quickshot.ui;

import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.io.File;


public class Widget extends Button {
    private ImageView iconA;
    private ImageView iconB;
    private int iconSize = 15;
    private Cursor cursorType;
    private boolean isDrawing;
    private boolean isSelected;
    private boolean isMouseOver;

    public Widget(String fileName, String toolTip) {
        setIcons(fileName);
        setTooltip(new Tooltip(toolTip));
        getStyleClass().add("button");
        setSelected(false);
        setMouseOver(false);
        setCursorType(Cursor.DEFAULT);
        defaultMouseEvents();
    }

    public void defaultMouseEvents() {
        // isSelected handled in ViewfinderWidgetBar
        setOnMouseEntered(e -> setMouseOver(true));
        setOnMouseExited(e -> setMouseOver(false));
    }

    /**
     * Sets both the icons for the widget
     * @param fileName: The file name without extension ('brush_dark.png', use: 'brush')
     */
    public void setIcons(String fileName) {
        Image imgA = new Image(String.valueOf(ClassLoader.getSystemResource(fileName + "_dark.png")));
        Image imgB = new Image(String.valueOf(ClassLoader.getSystemResource(fileName + "_light.png")));
        iconA = new ImageView(imgA);
        iconB = new ImageView(imgB);
        iconA.setPreserveRatio(true);
        iconB.setPreserveRatio(true);
        iconA.setFitWidth(getIconSize());
        iconA.setFitHeight(getIconSize());
        iconB.setFitWidth(getIconSize());
        iconB.setFitHeight(getIconSize());
        setGraphic(iconB);

    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        setFocused(selected);
        isSelected = selected;
        if (selected)
            setGraphic(iconA);
        else
            setGraphic(iconB);
    }

    public boolean isMouseOver() {
        return isMouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        isMouseOver = mouseOver;
    }

    public int getIconSize() {
        return iconSize;
    }

    public void setIconSize(int iconSize) {
        this.iconSize = iconSize;
        iconA.setFitWidth(iconSize);
        iconA.setFitHeight(iconSize);
        iconB.setFitWidth(iconSize);
        iconB.setFitHeight(iconSize);
    }

    public boolean isDrawing() {
        return isDrawing;
    }

    public void setDrawing(boolean drawing) {
        isDrawing = drawing;
    }

    public Cursor getCursorType() {
        return cursorType;
    }

    public void setCursorType(Cursor cursorType) {
        this.cursorType = cursorType;
    }

    public void setClickableOnly() {
        // dont change Pressed. Only works with Pressed.
        setOnMousePressed(e -> setFocused(false));
    }

    public void draw(MouseEvent mouseEvent) {
    }
}

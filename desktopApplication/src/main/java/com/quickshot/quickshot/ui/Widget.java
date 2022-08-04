/**
 * Widget.java
 * @Description: Widget is a base class that is preferred to be inherited from, although it can be used as is.
 * This class functions as a base to what is needed for the Widgets that form the toolbars.
 */

package com.quickshot.quickshot.ui;

import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.io.File;


public class Widget extends Button {
    private final ImageView icon;
    private int iconSize = 15;
    private Cursor cursorType;
    private boolean isDrawing;
    private boolean isSelected;
    private boolean isMouseOver;

    public Widget(String fileName) {
        File f = new File("src/main/java/com/quickshot/quickshot/resources/" + fileName);
        Image img = new Image(f.getAbsolutePath());
        icon = new ImageView(img);
        icon.setPreserveRatio(true);
        icon.setFitWidth(getIconSize());
        icon.setFitHeight(getIconSize());
        getStyleClass().add("button");
        setGraphic(icon);
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        setFocused(selected);
        isSelected = selected;
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
        icon.setFitWidth(iconSize);
        icon.setFitHeight(iconSize);
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

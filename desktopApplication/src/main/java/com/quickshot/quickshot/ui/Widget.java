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
    private ImageView iconActive;
    private ImageView iconUnactive;
    private int iconSize = 15;
    private Cursor cursorType;
    private boolean isDrawing;
    private boolean isSelected;
    private boolean isMouseOver;

    public Widget(String fileName) {
        setIcons(fileName);
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

    public void setIcons(String fileName) {
        File fileActive = new File("src/main/java/com/quickshot/quickshot/resources/" + fileName + "_dark.png");
        File fileUnactive = new File("src/main/java/com/quickshot/quickshot/resources/" + fileName + "_light.png");
        Image imgActive = new Image(fileActive.getAbsolutePath());
        Image imgUnactive = new Image(fileUnactive.getAbsolutePath());
        iconActive = new ImageView(imgActive);
        iconUnactive = new ImageView(imgUnactive);
        iconActive.setPreserveRatio(true);
        iconUnactive.setPreserveRatio(true);
        iconActive.setFitWidth(getIconSize());
        iconActive.setFitHeight(getIconSize());
        iconUnactive.setFitWidth(getIconSize());
        iconUnactive.setFitHeight(getIconSize());
        setGraphic(iconUnactive);

    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        setFocused(selected);
        isSelected = selected;
        if (selected)
            setGraphic(iconActive);
        else
            setGraphic(iconUnactive);
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
        iconActive.setFitWidth(iconSize);
        iconActive.setFitHeight(iconSize);
        iconUnactive.setFitWidth(iconSize);
        iconUnactive.setFitHeight(iconSize);
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

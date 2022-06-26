package com.quickshot.quickshot;

import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.nio.file.Path;

public abstract class Widget extends Button {
    private final ImageView icon;
    private int iconSize = 15;
    private boolean isSelected;
    private boolean isMouseOver;

    public Widget(String fileName) {
        File f = new File("src/main/java/com/quickshot/quickshot/icons/" + fileName).getAbsoluteFile();
        Image img = new Image(f.getAbsolutePath());
        icon = new ImageView(img);
        icon.setPreserveRatio(true);
        icon.setFitWidth(iconSize);
        icon.setFitHeight(iconSize);
        setGraphic(icon);
        setSelected(false);
        setMouseOver(false);
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

    public void draw(MouseEvent mouseEvent) {
    }
}

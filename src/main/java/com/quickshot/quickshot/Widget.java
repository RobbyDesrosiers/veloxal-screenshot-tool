package com.quickshot.quickshot;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.nio.file.Path;

// https://www.tutorialspoint.com/how-to-add-an-image-to-a-button-action-in-javafx
public abstract class Widget extends Button {
    private ImageView icon;
    private int iconSize = 15;
    private boolean isSelected = false;

    private boolean isMouseOver = false;

    public Widget(String fileName) {
        File f = new File("src/main/java/com/quickshot/quickshot/icons/" + fileName).getAbsoluteFile();
        Image img = new Image(f.getAbsolutePath());
        icon = new ImageView(img);
        icon.setPreserveRatio(true);
        icon.setFitWidth(iconSize);
        icon.setFitHeight(iconSize);
        setGraphic(icon);
        initMouseEvents();
    }

    public void initMouseEvents() {
        setOnMouseClicked(e -> {
            setSelected(!isSelected());
        });
        setOnMouseEntered(e -> {
            setMouseOver(true);
        });
        setOnMouseExited(e -> {
            setMouseOver(false);
        });
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isMouseOver() {
        return isMouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        isMouseOver = mouseOver;
    }

    public void draw(MouseEvent mouseEvent) {
    }
}

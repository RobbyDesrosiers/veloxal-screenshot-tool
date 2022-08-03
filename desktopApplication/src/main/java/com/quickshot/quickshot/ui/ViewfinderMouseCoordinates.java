package com.quickshot.quickshot.ui;

import com.quickshot.quickshot.utilities.Coordinate;
import com.quickshot.quickshot.utilities.DisplayElement;
import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ViewfinderMouseCoordinates extends VBox implements DisplayElement {
    private Text text = new Text();

    private Coordinate mouseCoordinates = new Coordinate();

    public ViewfinderMouseCoordinates() {
        text.setFill(Color.WHITE);
        text.setFont(Font.font("Verdana", FontWeight.LIGHT, 9));
        setCache(true);
        setCacheHint(CacheHint.SPEED);
        getChildren().add(text);
        setPadding(new Insets(1,3,3,3));
        setViewOrder(-1);
    }

    public void update(MouseEvent mouseEvent) {
        int OFFSET = 3;
        mouseCoordinates.setX(mouseEvent.getSceneX());
        mouseCoordinates.setY(mouseEvent.getSceneY());
        setTranslateY(mouseCoordinates.getY() + OFFSET);
        setTranslateX(mouseCoordinates.getX() + OFFSET);
        updateDimensions();
    }

    private void updateDimensions() {
        text.setText((int)mouseCoordinates.getX() + "\n" + (int)mouseCoordinates.getY());
    }

    @Override
    public void update() {
    }
}

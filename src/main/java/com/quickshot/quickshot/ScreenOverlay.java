package com.quickshot.quickshot;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ScreenOverlay extends Pane {
    final private Scene scene;

    public ScreenOverlay() {
        scene = new Scene(this);
    }

    public void setTestEnvironment(Stage stage) {
        stage.setScene(scene);
        setPrefSize(800, 600);
        stage.show();
    }

    public void setProductionEnvironment(Stage stage) {
        scene.setFill(new Color(0.0, 0.0, 0.0, 0.01));
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.show();
    }

    public void addToScreen(Node node) {
        getChildren().add(node);
    }

    public void removeFromScreen(Node node) {
        getChildren().remove(node);
    }

}

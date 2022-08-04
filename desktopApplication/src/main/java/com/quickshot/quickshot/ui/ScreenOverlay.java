package com.quickshot.quickshot.ui;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.File;

public class ScreenOverlay extends Pane {
    final private Scene scene;

    public ScreenOverlay() {
        scene = new Scene(this);
        File f = new File("src/main/java/com/quickshot/quickshot/resources/style.css");
        getScene().getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));
        // fixes bug where on viewfinder creation toolbar-widget is auto-selected
        requestFocus();
    }

    public void setTestEnvironment(Stage stage) {
        stage.setScene(scene);
        setPrefSize(800, 600);
    }

    public void setProductionEnvironment(Stage stage) {
        setDim();

        // fixes the weird color glitch when spawning buttons/toolbars
        setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        Platform.setImplicitExit(false);
    }

    public void setTransparent() {
        scene.setFill(new Color(0.0, 0.0, 0.0, 0.01));
    }

    public void setDim() {
        scene.setFill(new Color(0.0, 0.0, 0.0, 0.5));
    }

    public void addToScreen(Node node) {
        getChildren().add(node);
    }

    public void removeFromScreen(Node node) {
        getChildren().remove(node);
    }

}

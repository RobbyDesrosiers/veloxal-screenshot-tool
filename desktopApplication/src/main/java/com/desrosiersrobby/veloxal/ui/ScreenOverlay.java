/**
 * ScreenOverlay.java
 * @Description: The main Pane window for the program, this is where all the objects of viewfinder are drawn onto. This
 * is a transparent window pane with no features which opens full screen on the desktop.
 * UserController uses this class to detect mouse clicks from the user.
 */
package com.desrosiersrobby.veloxal.ui;

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
import java.net.URL;
import java.util.Objects;

public class ScreenOverlay extends Pane {
    final private Scene scene;

    public ScreenOverlay() {
        scene = new Scene(this);

        // stylesheet for the entire program
        getScene().getStylesheets().add(ClassLoader.getSystemResource("style.css").toExternalForm());
        // fixes bug where on viewfinder creation toolbar-widget is auto-selected
        requestFocus();
    }

    /**
     * Sets the stage to a 800x600 opaque window for debugging purposes
     * @param stage
     */
    public void setTestEnvironment(Stage stage) {
        stage.setScene(scene);
        setPrefSize(800, 600);
    }

    /**
     * The production window/stage of ScreenOverlay. This creates a full screen window without any buttons/utilities
     * associates with it
     * @param stage: Passed from main
     */
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

    /**
     * Sets the stage/window with a completely transparent background fill
     */
    public void setTransparent() {
        scene.setFill(new Color(0.0, 0.0, 0.0, 0.01));
    }

    /**
     * Sets the background to a background fill of 0.5 (alpha) to mock the exact transparency of the NegativeSpace class
     * used for the viewfinder
     */
    public void setDim() {
        scene.setFill(new Color(0.0, 0.0, 0.0, 0.5));
    }

    /**
     * Used to add nodes onto the window
     * @param node: Node to add onto the window
     */
    public void addToScreen(Node node) {
        getChildren().add(node);
    }

    /**
     * Used to remove nodes from the window
     * @param node: Node to remove from the window
     */
    public void removeFromScreen(Node node) {
        getChildren().remove(node);
    }

}

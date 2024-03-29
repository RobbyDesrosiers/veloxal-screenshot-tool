/**
 * Main.java
 * @Description: Main entry point of the program, see notes below
 */
package com.desrosiersrobby.veloxal;

import com.desrosiersrobby.veloxal.controllers.UserController;
import com.desrosiersrobby.veloxal.ui.ScreenOverlay;
import com.desrosiersrobby.veloxal.utilities.ProgramTray;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// properly build
//https://stackoverflow.com/questions/1082580/how-to-build-jars-from-intellij-properly

public class Main extends Application {
    private final int LEFT_CLICK = 1;
    @Override
    public void start(Stage stage) throws Exception {
        // sets up the system tray icon and functionality (see class for more details)
        ProgramTray programTray = new ProgramTray();

        // screen overlay is the transparent window that lays overtop of the desktop so graphical elem can be drawn onto
        ScreenOverlay screenOverlay = new ScreenOverlay();
        screenOverlay.setProductionEnvironment(stage);

        // the main controller for the program
        new UserController(screenOverlay, programTray);

        // allows the user to click the tray icon to 'start' the program
        programTray.getTrayIcon().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == LEFT_CLICK) {
                    Platform.runLater(() -> {
                        stage.show();
                        screenOverlay.setDim();
                        stage.setAlwaysOnTop(true);
                    });
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package com.quickshot.quickshot;

import com.quickshot.quickshot.controllers.UserController;
import com.quickshot.quickshot.ui.ScreenOverlay;
import com.quickshot.quickshot.utilities.ProgramTray;
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
        ProgramTray programTray = new ProgramTray();
        ScreenOverlay screenOverlay = new ScreenOverlay();
        screenOverlay.setProductionEnvironment(stage);
        new UserController(screenOverlay, programTray);

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
}

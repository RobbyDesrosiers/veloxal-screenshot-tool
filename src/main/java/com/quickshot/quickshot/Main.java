package com.quickshot.quickshot;

import com.quickshot.quickshot.controllers.UserController;
import com.quickshot.quickshot.ui.ScreenOverlay;
import com.quickshot.quickshot.utilities.ProgramTray;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main extends Application {
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
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        stage.show();
                        stage.setAlwaysOnTop(true);
                    }
                });
            }
        });
    }
}

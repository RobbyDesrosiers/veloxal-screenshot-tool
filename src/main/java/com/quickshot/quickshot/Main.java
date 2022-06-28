package com.quickshot.quickshot;

import com.quickshot.quickshot.controllers.UserController;
import com.quickshot.quickshot.ui.ScreenOverlay;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        ScreenOverlay screenOverlay = new ScreenOverlay();
        screenOverlay.setTestEnvironment(stage);
        new UserController(screenOverlay);
    }
}

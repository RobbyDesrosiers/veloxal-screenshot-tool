package com.quickshot.quickshot;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        ScreenOverlay screenOverlay = new ScreenOverlay();
        screenOverlay.setProductionEnvironment(stage);
        new UserController(screenOverlay);
    }
}

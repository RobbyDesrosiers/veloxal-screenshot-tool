package com.quickshot.quickshot;

import javafx.application.Application;
import javafx.stage.Stage;

// lag? https://stackoverflow.com/questions/10682107/correct-way-to-move-a-node-by-dragging-in-javafx-2
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        ScreenOverlay screenOverlay = new ScreenOverlay();
        screenOverlay.setProductionEnvironment(stage);
        new UserController(screenOverlay);
    }
}

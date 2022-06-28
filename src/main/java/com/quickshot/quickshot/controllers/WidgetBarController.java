package com.quickshot.quickshot.controllers;


import com.quickshot.quickshot.ui.Widget;
import com.quickshot.quickshot.ui.WidgetBarCommandTools;
import com.quickshot.quickshot.ui.WidgetBarDrawingTools;
import com.quickshot.quickshot.utilities.ScreenshotUtility;
import com.quickshot.quickshot.utilities.WidgetDrawData;
import javafx.application.Platform;

import java.awt.*;
import java.io.IOException;
import java.util.LinkedList;

public class WidgetBarController {
    private final WidgetBarDrawingTools drawingToolBar;
    private final WidgetBarCommandTools commandToolBar;
    private final WidgetDrawData drawData;
    private final ScreenshotUtility screenshotUtility;

    public WidgetBarController(ViewfinderController viewfinderController) {
        this.screenshotUtility = new ScreenshotUtility(viewfinderController);
        drawData = new WidgetDrawData();
        drawingToolBar = new WidgetBarDrawingTools(viewfinderController.getBoundingBox(), drawData);
        commandToolBar = new WidgetBarCommandTools(viewfinderController.getBoundingBox(), drawData);
        initMouseEvents();
    }

    private void initMouseEvents() {
        getDrawingToolBar().getUndoButton().setOnMouseReleased(e -> handleUndoButton());
        getCommandToolBar().getSaveButton().setOnMouseClicked(e -> handleSaveButton());
        getCommandToolBar().getCloseButton().setOnMouseClicked(e -> Platform.exit());
    }

    private void handleUndoButton() {
        Widget selectedWidget = getDrawingToolBar().getSelectedWidget();
        // moves the latest permanent drawn data into a deleted data list which will be removed from within
        // UserController -> refreshScreen() function
        if (getDrawData().size() > 0)
            getDrawData().getDeletedData().push(new LinkedList<>(getDrawData().pop()));
        if (selectedWidget != null)
            selectedWidget.setSelected(true);
    }

    private void handleSaveButton() {
        try {
            screenshotUtility.saveToFile();
        } catch (AWTException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void update() {
        getDrawingToolBar().update();
        getCommandToolBar().update();
    }
    public WidgetDrawData getDrawData() {
        return drawData;
    }

    public WidgetBarDrawingTools getDrawingToolBar() {
        return drawingToolBar;
    }

    public WidgetBarCommandTools getCommandToolBar() {
        return commandToolBar;
    }

    public boolean isMouseOver() {
        return getDrawingToolBar().isMouseOver() || getCommandToolBar().isMouseOver();
    }
}

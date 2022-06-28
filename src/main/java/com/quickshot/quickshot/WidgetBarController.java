package com.quickshot.quickshot;


import javafx.application.Platform;

import java.awt.*;
import java.io.IOException;
import java.util.LinkedList;

public class WidgetBarController {
    private final WidgetBarDrawingTools drawingToolBar;
    private final WidgetBarCommandTools commandToolBar;
    private final WidgetDrawData drawData;
    private final ScreenshotUtility screenshotUtility;

    public WidgetBarController(Viewfinder viewfinder) {
        this.screenshotUtility = new ScreenshotUtility(viewfinder);
        drawData = new WidgetDrawData();
        drawingToolBar = new WidgetBarDrawingTools(viewfinder.getBoundingBox(), drawData);
        commandToolBar = new WidgetBarCommandTools(viewfinder.getBoundingBox(), drawData);
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

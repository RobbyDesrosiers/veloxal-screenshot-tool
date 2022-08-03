/**
 * WidgetBarController.java
 * @Description: Used to encapsulate the two toolbars/widget bars that hold all drawing functionality and commands
 */

package com.quickshot.quickshot.controllers;


import com.quickshot.quickshot.ui.Widget;
import com.quickshot.quickshot.ui.WidgetBarCommandTools;
import com.quickshot.quickshot.ui.WidgetBarDrawingTools;
import com.quickshot.quickshot.utilities.ScreenshotUtility;
import com.quickshot.quickshot.utilities.WidgetDrawData;
import java.awt.*;
import java.io.IOException;
import java.util.LinkedList;

public class WidgetBarController {
    private final WidgetBarDrawingTools drawingToolBar;
    private final WidgetBarCommandTools commandToolBar;
    private final WidgetDrawData drawData;
    private final ScreenshotUtility screenshotUtility;

    public WidgetBarController(ViewfinderController viewfinderController) {
        // inits all class objects
        drawData = new WidgetDrawData();
        drawingToolBar = new WidgetBarDrawingTools(viewfinderController.getBoundingBox(), drawData);
        commandToolBar = new WidgetBarCommandTools(viewfinderController.getBoundingBox(), drawData);
        screenshotUtility = new ScreenshotUtility(viewfinderController);
        initMouseEvents();
    }

    /**
     * Initializes all mouse events the CommandToolbar and handles associated mouse clicks
     */
    private void initMouseEvents() {
        getDrawingToolBar().getUndoButton().setOnMouseReleased(e -> handleUndoButton());
        getCommandToolBar().getSaveButton().setOnMouseClicked(e -> handleSaveButton());
        getCommandToolBar().getCopyScreenshot().setOnMouseClicked(e -> handleCopyButton());
        getCommandToolBar().getUploadScreenshot().setOnMouseClicked(e -> {
            try {
                handleUploadButton();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        getCommandToolBar().getReadText().setOnMouseClicked(e -> handleReadTextButton());
//        getCommandToolBar().getRecordGif().setOnMouseClicked(e -> handleGifRecordButton());
    }

    /**
     * Handles the button press for the 'Read Text Button' and activates the appropriate function
     */
    private void handleReadTextButton() {
        getScreenshotUtility().readTextFromImage();
    }

    /**
     * Handles the button press for the 'Upload Button' and activates the appropriate function
     */
    private void handleUploadButton() throws IOException {
        getScreenshotUtility().uploadScreenshot();
    }

    /**
     * Handles the 'Undo Button' when clicked by user
     */
    private void handleUndoButton() {
        Widget selectedWidget = getDrawingToolBar().getSelectedWidget();
        // moves the latest permanent drawn data into a deleted data list which will be removed from within
        // UserController -> refreshScreen() function
        if (getDrawData().size() > 0)
            getDrawData().getDeletedData().push(new LinkedList<>(getDrawData().pop()));
        if (selectedWidget != null)
            selectedWidget.setSelected(true);
    }

    /**
     * Handles the 'Save Button' when clicked by user
     */
    private void handleSaveButton() {
        try {
            getScreenshotUtility().saveSingleScreenshotToFile();
        } catch (AWTException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Handles the 'Copy To Clipboard Button' when clicked by user
     */
    private void handleCopyButton() {
        getScreenshotUtility().saveSingleScreenshotToClipboard();
    }

    private void handleGifRecordButton() {
        getScreenshotUtility().recordScreen(3);
    }

    /**
     * Updates the new positions for the toolbars for screenRefresh functionality in UserController
     */
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

    public ScreenshotUtility getScreenshotUtility() {
        return screenshotUtility;
    }

    public boolean isMouseOver() {
        return getDrawingToolBar().isMouseOver() || getCommandToolBar().isMouseOver();
    }
}

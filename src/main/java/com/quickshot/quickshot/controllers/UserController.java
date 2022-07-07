package com.quickshot.quickshot.controllers;

import com.quickshot.quickshot.utilities.DisplayElement;
import com.quickshot.quickshot.ui.ScreenOverlay;
import com.quickshot.quickshot.utilities.ProgramTray;
import com.quickshot.quickshot.utilities.ViewfinderAnchorPosition;
import com.quickshot.quickshot.ui.Widget;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import java.awt.*;
import java.util.LinkedList;

public class UserController {
    private final ScreenOverlay screenOverlay;
    private final ViewfinderController viewfinderController;
    private final ProgramTray programTray;


    public UserController(ScreenOverlay screenOverlay, ProgramTray programTray) throws AWTException {
        this.screenOverlay = screenOverlay;
        this.screenOverlay.getScene().setCursor(Cursor.CROSSHAIR);
        this.programTray = programTray;
        viewfinderController = new ViewfinderController(programTray);
        initMouseEvents();
        initKeyboardEvents();
    }

    private void initMouseEvents() {
        // used primarily for hover/cursor changes
        screenOverlay.getScene().addEventFilter(MouseEvent.MOUSE_MOVED, this::handleMouseMoved);

        // regular mouse events, used usually on start up when viewfinder is not visible
        screenOverlay.setOnMousePressed(this::handleMousePressed);
        screenOverlay.setOnMouseReleased(this::handleMouseReleased);
        screenOverlay.setOnMouseDragged(this::handleMouseDragged);

        // mouse events below used for when viewfinder is created and user is forced to click/drag/release
        // on the dimmed ViewfinderNegativeSpaceList objects instead of the ScreenOverlay obj
        viewfinderController.getNegativeSpace().setOnMouseDragged(this::handleMouseDragged);
        viewfinderController.getNegativeSpace().setOnMousePressed(this::handleMousePressed);
        viewfinderController.getNegativeSpace().setOnMouseReleased(this::handleMouseReleased);

        // allows screen to be refreshed and drawing to be removed on undo
        viewfinderController.getWidgetController().getDrawingToolBar().getUndoButton().setOnMouseClicked(e -> refreshScreen());

        // close button
        viewfinderController.getWidgetController().getCommandToolBar().getCloseButton().setOnMouseClicked(e -> minimiseProgram());

        // program exit
        programTray.getExitMenuButton().addActionListener(e -> System.exit(0));
    }

    private void minimiseProgram() {
        viewfinderController.hideStage();
        refreshScreen();
    }

    private void initKeyboardEvents() {
        screenOverlay.getScene().setOnKeyPressed(this::handleKeyPressed);
    }

    private void handleKeyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case ESCAPE -> {
                minimiseProgram();
            }
            case F -> {
                if (keyEvent.isShiftDown()) {
                    viewfinderController.enterFullScreen();
                }
            }
        }
        refreshScreen();
    }

    private void handleMouseMoved(MouseEvent mouseEvent) {
        // sets all widgets 'isDrawing' field to false (used to determine undo button usage in refreshScreen())
        viewfinderController.getWidgetController().getDrawingToolBar().setWidgetsDrawingStatus(false);
        // deals with all the on-hover mouse changes
        if (viewfinderController.getWidgetController().getDrawingToolBar().isWidgetSelected()) {
            screenOverlay.getScene().setCursor(viewfinderController.getWidgetController().getDrawingToolBar().getSelectedWidget().getCursorType());
        } else if (viewfinderController.getAnchors().isMouseOver()) {
            screenOverlay.getScene().setCursor(viewfinderController.getAnchors().getHoveredAnchorCursorType());
        } else if (viewfinderController.getBoundingBox().isMouseOver()) {
            screenOverlay.getScene().setCursor(Cursor.MOVE);
        } else if (viewfinderController.getWidgetController().isMouseOver()) {
            screenOverlay.getScene().setCursor(Cursor.DEFAULT);
        } else {
            screenOverlay.getScene().setCursor(Cursor.CROSSHAIR);
        }
        refreshScreen();
    }

    private void handleMousePressed(MouseEvent mouseEvent) {
        if (!viewfinderController.isCreated() && viewfinderController.getMovementAllowed()) {
            viewfinderController.createViewfinder(mouseEvent);
            screenOverlay.setTransparent();
        }

        // if not selected the viewfinder will be deleted and a new one will spawn where the mouse clicks
        if (!viewfinderController.isSelected() && !viewfinderController.getAnchors().isSelected() && viewfinderController.getMovementAllowed()) {
            viewfinderController.createViewfinder(mouseEvent);
            screenOverlay.setTransparent();
        } else if (viewfinderController.getMovementAllowed()){
            // if viewfinder is selected then mouse will drag
            // sets the offset of mouse within the box to accurately pan viewfinder
            viewfinderController.setDragPoint(mouseEvent);
        }
        refreshScreen();
    }

    private void handleMouseDragged(MouseEvent mouseEvent) {
        // handles all widget drawing
        if (viewfinderController.getWidgetController().getDrawingToolBar().isWidgetSelected()) {
            Widget widget = viewfinderController.getWidgetController().getDrawingToolBar().getSelectedWidget();
            widget.draw(mouseEvent);
            widget.setDrawing(true);
        // handles all the viewfinder scaling if an anchor is selected
        } else if (viewfinderController.getAnchors().isSelected() && viewfinderController.getMovementAllowed()) {
            ViewfinderAnchorPosition selectedAnchorPosition = viewfinderController.getAnchors().getSelectedAnchorPosition();
            switch (selectedAnchorPosition) {
                case TOP_LEFT -> viewfinderController.getBoundingBox().moveUpLeft(mouseEvent);
                case TOP_MIDDLE -> viewfinderController.getBoundingBox().moveUp(mouseEvent);
                case TOP_RIGHT -> viewfinderController.getBoundingBox().moveUpRight(mouseEvent);
                case MIDDLE_LEFT -> viewfinderController.getBoundingBox().moveLeft(mouseEvent);
                case MIDDLE_RIGHT -> viewfinderController.getBoundingBox().moveRight(mouseEvent);
                case BOTTOM_LEFT -> viewfinderController.getBoundingBox().moveDownLeft(mouseEvent);
                case BOTTOM_MIDDLE -> viewfinderController.getBoundingBox().moveDown(mouseEvent);
                case BOTTOM_RIGHT -> viewfinderController.getBoundingBox().moveDownRight(mouseEvent);
            }
        // handles the panning/moving of the viewfinder if viewfinder is selected
        } else if (viewfinderController.isSelected() && viewfinderController.getMovementAllowed()) {
            viewfinderController.move(mouseEvent);
        }
        refreshScreen();
    }

    private void handleMouseReleased(MouseEvent mouseEvent) {
        // fixes on creation drag bug which caused bottom-right anchor to be selected all the time
        // this is due to bottom-right being auto selected (true) on creation for dragging capabilities
        viewfinderController.getAnchors().deselectAllAnchors();

        // lets drawing tools know to start a new line if mouse gets released
        if (viewfinderController.getWidgetController().getDrawingToolBar().isWidgetSelected()) {
            viewfinderController.getWidgetController().getDrawData().setNewLine(true);
        }
        refreshScreen();
    }

    public void refreshScreen() {
        if (!viewfinderController.isCreated())
            return;

        // when user is done drawing the temp data drawn to screen gets offloaded to a perm data source(the Draw Data Obj)
        // this allows user to undo brush-strokes/full drawn objects
        if (!viewfinderController.getWidgetController().getDrawingToolBar().isWidgetDrawing() && viewfinderController.getWidgetController().getDrawData().getTempData().size() > 0) {
            viewfinderController.getWidgetController().getDrawData().saveTempDataToDrawData();
        }

        // temp fix. when user clicks on a different control panel the drawing panel loses focus even if widget is still
        // selected todo fix this
        if (viewfinderController.getWidgetController().getDrawingToolBar().isWidgetSelected()) {
            Widget w = viewfinderController.getWidgetController().getDrawingToolBar().getSelectedWidget();
            w.requestFocus();
        }

        // adds all displayElements to viewfinder if not already added
        for (DisplayElement viewFinderElement : viewfinderController.getDisplayElements()) {
            if (!screenOverlay.getChildren().contains((Node) viewFinderElement)) {
                screenOverlay.addToScreen((Node) viewFinderElement);
            }
        }

        // adds temp data to screen
        for (Node node : viewfinderController.getWidgetController().getDrawData().getTempData()) {
            if (!screenOverlay.getChildren().contains(node)) {
                screenOverlay.addToScreen(node);
            }
        }

        // removes deleted data from screen
        if (viewfinderController.getWidgetController().getDrawData().getDeletedData().size() > 0) {
            for (LinkedList<Node> deletedData : viewfinderController.getWidgetController().getDrawData().getDeletedData()) {
                for (Node node : deletedData) {
                    screenOverlay.removeFromScreen(node);
                }
            }
            viewfinderController.getWidgetController().getDrawData().getDeletedData().pop();
        }

        viewfinderController.checkViewfinderInversion();
        viewfinderController.update();
    }
}

package com.quickshot.quickshot;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.LinkedList;

public class UserController {
    private final ScreenOverlay screenOverlay;
    private Viewfinder viewfinder;

    public UserController(ScreenOverlay screenOverlay) {
        this.screenOverlay = screenOverlay;
        this.screenOverlay.getScene().setCursor(Cursor.CROSSHAIR);
        viewfinder = new Viewfinder();
        initMouseEvents();
        initKeyboardEvents();
    }

    private void initMouseEvents() {
        screenOverlay.getScene().addEventFilter(MouseEvent.MOUSE_MOVED, this::handleMouseMoved);

        screenOverlay.setOnMousePressed(this::handleMousePressed);
        screenOverlay.setOnMouseReleased(this::handleMouseReleased);
        screenOverlay.setOnMouseDragged(this::handleMouseDragged);

        // mouse events below used for when viewfinder is created and user is forced to click/drag/release
        // on the dimmed ViewfinderNegativeSpaceList objects instead of the ScreenOverlay obj
        viewfinder.getNegativeSpace().setOnMouseDragged(this::handleMouseDragged);
        viewfinder.getNegativeSpace().setOnMousePressed(this::handleMousePressed);
        viewfinder.getNegativeSpace().setOnMouseReleased(this::handleMouseReleased);

        // allows screen to be refreshed and drawing to be removed on undo
        viewfinder.getWidgetBar().getUndoButton().setOnMouseClicked(e -> refreshScreen());
    }

    private void initKeyboardEvents() {
        screenOverlay.getScene().setOnKeyPressed(this::handleKeyPressed);
    }

    private void handleKeyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case ESCAPE -> {
                if (keyEvent.isShiftDown()) {
                    viewfinder.setVisible(false);
                    viewfinder.getWidgetBar().getDrawData().setVisible(false);
                } else {
                    Platform.exit();
                }
            }
            case F -> {
                if (keyEvent.isShiftDown()) {
                    viewfinder.enterFullScreen();
                }
            }
        }
        refreshScreen();
    }

    private void handleMouseMoved(MouseEvent mouseEvent) {
        // sets all widgets 'isDrawing' field to false (used to determine undo button usage in refreshScreen())
        viewfinder.getWidgetBar().setWidgetDrawingStatus(false);
        // deals with all the on-hover mouse changes
        if (viewfinder.getWidgetBar().isWidgetSelected()) {
            screenOverlay.getScene().setCursor(viewfinder.getWidgetBar().getSelectedWidget().getCursorType());
        } else if (viewfinder.getAnchors().isMouseOver()) {
            screenOverlay.getScene().setCursor(viewfinder.getAnchors().getHoveredAnchorCursorType());
        } else if (viewfinder.getBoundingBox().isMouseOver()) {
            screenOverlay.getScene().setCursor(Cursor.MOVE);
        } else if (viewfinder.getWidgetBar().isMouseOver()) {
            screenOverlay.getScene().setCursor(Cursor.DEFAULT);
        } else {
            screenOverlay.getScene().setCursor(Cursor.CROSSHAIR);
        }
        refreshScreen();
    }

    private void handleMousePressed(MouseEvent mouseEvent) {
        if (!viewfinder.isCreated()) {
            viewfinder.createViewfinder(mouseEvent);
        }
        // if not selected the viewfinder will be deleted and a new one will spawn where the mouse clicks
        if (!viewfinder.isSelected() && !viewfinder.getAnchors().isSelected()) {
            viewfinder.createViewfinder(mouseEvent);
        } else {
            // if viewfinder is selected then mouse will drag
            // sets the offset of mouse within the box to accurately pan viewfinder
            viewfinder.setDragPoint(mouseEvent);
        }
        refreshScreen();
    }

    private void handleMouseDragged(MouseEvent mouseEvent) {
        // handles all widget drawwing
        if (viewfinder.getWidgetBar().isWidgetSelected()) {
            Widget widget = viewfinder.getWidgetBar().getSelectedWidget();
            widget.draw(mouseEvent);
            widget.setDrawing(true);
        // handles all the viewfinder scaling if an anchor is selected
        } else if (viewfinder.getAnchors().isSelected()) {
            ViewfinderAnchorPosition selectedAnchorPosition = viewfinder.getAnchors().getSelectedAnchorPosition();
            switch (selectedAnchorPosition) {
                case TOP_LEFT -> viewfinder.getBoundingBox().moveUpLeft(mouseEvent);
                case TOP_MIDDLE -> viewfinder.getBoundingBox().moveUp(mouseEvent);
                case TOP_RIGHT -> viewfinder.getBoundingBox().moveUpRight(mouseEvent);
                case MIDDLE_LEFT -> viewfinder.getBoundingBox().moveLeft(mouseEvent);
                case MIDDLE_RIGHT -> viewfinder.getBoundingBox().moveRight(mouseEvent);
                case BOTTOM_LEFT -> viewfinder.getBoundingBox().moveDownLeft(mouseEvent);
                case BOTTOM_MIDDLE -> viewfinder.getBoundingBox().moveDown(mouseEvent);
                case BOTTOM_RIGHT -> viewfinder.getBoundingBox().moveDownRight(mouseEvent);
            }
        // handles the panning/moving of the viewfinder if viewfinder is selected
        } else if (viewfinder.isSelected()) {
            viewfinder.move(mouseEvent);
        }
        refreshScreen();
    }

    private void handleMouseReleased(MouseEvent mouseEvent) {
        // fixes on creation drag bug which caused bottom-right anchor to be selected all the time
        // this is due to bottom-right being auto selected (true) on creation for dragging capabilities
        viewfinder.getAnchors().deselectAllAnchors();

        if (viewfinder.getWidgetBar().isWidgetSelected()) {
            viewfinder.getWidgetBar().getDrawData().setNewLine(true);
        }
        refreshScreen();
    }

    public void refreshScreen() {
        if (!viewfinder.isCreated())
            return;

        // when user is done drawing the temp data drawn to screen gets offloaded to a perm data source(the Draw Data Obj)
        // this allows user to undo brush-strokes/full drawn objects
        if (!viewfinder.getWidgetBar().isWidgetDrawing() && viewfinder.getWidgetBar().getDrawData().getTempData().size() > 0) {
            viewfinder.getWidgetBar().getDrawData().saveTempDataToDrawData();
        }

        // adds all displayElements to viewfinder if not already added
        for (DisplayElement viewFinderElement : viewfinder.getDisplayElements()) {
            if (!screenOverlay.getChildren().contains((Node) viewFinderElement)) {
                screenOverlay.addToScreen((Node) viewFinderElement);
            }
        }

        // adds temp data to screen
        for (Node node : viewfinder.getWidgetBar().getDrawData().getTempData()) {
            if (!screenOverlay.getChildren().contains(node)) {
                screenOverlay.addToScreen(node);
            }
        }

        // removes deleted data from screen
        if (viewfinder.getWidgetBar().getDrawData().getDeletedData().size() > 0) {
            for (LinkedList<Node> deletedData : viewfinder.getWidgetBar().getDrawData().getDeletedData()) {
                for (Node node : deletedData) {
                    screenOverlay.removeFromScreen(node);
                }
            }
            viewfinder.getWidgetBar().getDrawData().getDeletedData().pop();
        }

        viewfinder.checkViewfinderInversion();
        viewfinder.update();
    }
}

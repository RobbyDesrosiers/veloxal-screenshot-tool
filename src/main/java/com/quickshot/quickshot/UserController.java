package com.quickshot.quickshot;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class UserController {
    private final ScreenOverlay screenOverlay;
    private Viewfinder viewfinder;

    public UserController(ScreenOverlay screenOverlay) {
        this.screenOverlay = screenOverlay;
        this.screenOverlay.getScene().setCursor(Cursor.CROSSHAIR);
        initMouseEvents();
        initKeyboardEvents();
    }

    private void initMouseEvents() {
        screenOverlay.setOnMousePressed(this::handleMousePressed);
        screenOverlay.setOnMouseReleased(this::handleMouseReleased);
        screenOverlay.setOnMouseDragged(this::handleMouseDragged);
        screenOverlay.setOnMouseMoved(this::handleMouseMoved);
    }

    private void initKeyboardEvents() {
        screenOverlay.getScene().setOnKeyPressed(this::handleKeyPressed);
    }

    private void handleKeyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case ESCAPE -> Platform.exit();
        }
    }

    private void handleMouseMoved(MouseEvent mouseEvent) {
        if (viewfinder == null) {
            return;
        }

        // deals with all the on-hover mouse changes
        if (viewfinder.getAnchors().isMouseOver()) {
            screenOverlay.getScene().setCursor(viewfinder.getAnchors().getHoveredAnchorCursorType());
        } else if (viewfinder.getBoundingBox().isMouseOver()) {
            screenOverlay.getScene().setCursor(Cursor.MOVE);
        } else {
            screenOverlay.getScene().setCursor(Cursor.CROSSHAIR);
        }

    }

    private void handleMousePressed(MouseEvent mouseEvent) {
        if (viewfinder == null)
            viewfinder = new Viewfinder(mouseEvent);
        // if not selected the viewfinder will be deleted and a new one will spawn where the mouse clicks
        if (!viewfinder.isSelected(mouseEvent) && !viewfinder.getAnchors().isSelected()) {
            removeViewfinder();
            viewfinder = new Viewfinder(mouseEvent);
        } else {
            // if viewfinder is selected then mouse will drag
            // sets the offset of mouse within the box to accurately pan viewfinder
            viewfinder.setDragPoint(mouseEvent);
        }

        updateViewfinder();
    }

    private void handleMouseDragged(MouseEvent mouseEvent) {

        if (viewfinder.getAnchors().isSelected()) {
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
        } else if (viewfinder.isSelected(mouseEvent)) {
            viewfinder.move(mouseEvent);
        }
        updateViewfinder();
    }

    private void handleMouseReleased(MouseEvent mouseEvent) {
        // fixes on creation drag bug which caused bottom-right anchor to be selected all the time
        // this is due to bottom-right being auto selected (true) on creation for dragging capabilities
        viewfinder.getAnchors().deselectAllAnchors();

        updateViewfinder();
    }

    private void removeViewfinder() {
        if (this.viewfinder != null) {
            // loops through entire viewfinder elements and removes from screen overlay
            // used to refresh the view by deleting all before .update runs to .add each element back
            for (DisplayElement viewFinderElement : viewfinder.getDisplayElements()) {
                screenOverlay.removeFromScreen((Node) viewFinderElement);
            }
        }
    }

    public void updateViewfinder() {
        // adds all displayElements to viewfinder.. probably should offload this somewhere else
        for (DisplayElement viewFinderElement : viewfinder.getDisplayElements()) {
            if (!screenOverlay.getChildren().contains((Node) viewFinderElement)) {
                screenOverlay.addToScreen((Node) viewFinderElement);
            }
        }
        viewfinder.checkViewfinderInversion();
        viewfinder.update();
    }

}
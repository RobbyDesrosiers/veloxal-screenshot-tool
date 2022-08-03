package com.quickshot.quickshot.controllers;

import com.quickshot.quickshot.ui.ViewfinderBoundingBox;
import com.quickshot.quickshot.ui.ViewfinderDimensions;
import com.quickshot.quickshot.utilities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ViewfinderController {
    private final ViewfinderBoundingBox boundingBox;
    private final ViewfinderAnchorList anchors;
    private final ViewfinderDimensions dimensions;
    private final ViewfinderNegativeSpaceList negativeSpace;
    private final WidgetBarController widgetController;
    private final ObservableList<DisplayElement> viewFinderElements = FXCollections.observableArrayList();
    private final ProgramTray programTray;
    private boolean isCreated;
    private boolean isMovementAllowed;
    private boolean isMousePassthroughOn;

    public ViewfinderController(ProgramTray programTray) {
        boundingBox = new ViewfinderBoundingBox();
        anchors = new ViewfinderAnchorList();
        dimensions = new ViewfinderDimensions();
        negativeSpace = new ViewfinderNegativeSpaceList();
        widgetController = new WidgetBarController(this);
        this.programTray = programTray;

        // adding items to viewFinderElements allows to render onto screen via UserControls -> screenRefresh
        viewFinderElements.add(boundingBox);
        viewFinderElements.addAll(anchors);
        viewFinderElements.add(dimensions);
        viewFinderElements.addAll(negativeSpace);
        viewFinderElements.add(widgetController.getDrawingToolBar());
        viewFinderElements.add(widgetController.getCommandToolBar());
        setCreated(false);
        allowMovement(true);
    }

    public void createViewfinder(MouseEvent mouseEvent) {
        getBoundingBox().resetViewfinderBoundingBox(mouseEvent);
        getAnchors().setBoundingBox(boundingBox);
        getDimensions().setBoundingBox(boundingBox);
        getNegativeSpace().setBoundingBox(boundingBox);
        setCreated(true);

        // these two lines replicate the events of creating a new viewfinder so the mouse 'selects' the bottom right
        // for dragging capabilities on creation
        getAnchors().getAnchor(ViewfinderAnchorPosition.BOTTOM_RIGHT).setSelected(true);
        setVisible(true);
        getBoundingBox().requestFocus();
    }

    public void update() {
        getBoundingBox().update();
        getAnchors().update();
        getDimensions().update();
        getNegativeSpace().update();
        getWidgetController().update();
    }

    public void setVisible(boolean b) {
        getBoundingBox().setVisible(b);
        getAnchors().setVisible(b);
        getDimensions().setVisible(b);
        getNegativeSpace().setVisible(b);
        getWidgetController().getDrawingToolBar().setVisible(b);
        getWidgetController().getCommandToolBar().setVisible(b);
    }

    public void setVisibilityForScreenshot(boolean b) {
        getAnchors().setVisible(b);
        getBoundingBox().setVisible(b);
        getDimensions().setVisible(b);
        getWidgetController().getDrawingToolBar().setVisible(b);
        getWidgetController().getCommandToolBar().setVisible(b);
    }

    public ViewfinderBoundingBox getBoundingBox() {
        return boundingBox;
    }

    public ObservableList<DisplayElement> getDisplayElements() {
        return viewFinderElements;
    }

    public ViewfinderAnchorList getAnchors() {
        return anchors;
    }

    public boolean isSelected() {
        return getBoundingBox().isSelected();
    }

    public boolean isCreated() {
        return isCreated;
    }

    public void setCreated(boolean created) {
        isCreated = created;
    }

    public WidgetBarController getWidgetController() {
        return widgetController;
    }

    public ViewfinderNegativeSpaceList getNegativeSpace() {
        return negativeSpace;
    }

    private ViewfinderDimensions getDimensions() {
        return dimensions;
    }


    public void setDragPoint(MouseEvent mouseEvent) {
        getBoundingBox().setDragPoint(mouseEvent);
    }

    public void move(MouseEvent mouseEvent) {
        getBoundingBox().move(mouseEvent);
    }

    public void checkViewfinderInversion() {
        ViewfinderAnchorPosition selectedAnchor = getAnchors().getSelectedAnchorPosition();
        if (selectedAnchor == null)
            return;

        // checks if the box's right side crosses over the left
        // the box acted weird due to the latency of mouse-movement, moving left side over right acts
        // differently than right over left. Basically by utilizing the bounding box we can stabilize the
        // 'anchor' point and swap selected anchors cleanly
        if (getBoundingBox().getMiddleLeft().getX() > getBoundingBox().getMiddleRight().getX()) {
            getAnchors().deselectAllAnchors();
            switch(selectedAnchor) {
                case TOP_LEFT -> {
                    getAnchors().selectAnchor(ViewfinderAnchorPosition.TOP_RIGHT);
                    // this part is necessary to stabilize the anchoring side and ensure it stays in position
                    // as it just sets the left side of the box to equal where the right was
                    getBoundingBox().setTranslateX(getBoundingBox().getMiddleRight().getX());
                }
                case MIDDLE_LEFT -> {
                    getAnchors().selectAnchor(ViewfinderAnchorPosition.MIDDLE_RIGHT);
                    getBoundingBox().setTranslateX(getBoundingBox().getMiddleRight().getX());
                }
                case BOTTOM_LEFT -> {
                    getAnchors().selectAnchor(ViewfinderAnchorPosition.BOTTOM_RIGHT);
                    getBoundingBox().setTranslateX(getBoundingBox().getMiddleRight().getX());
                }
                case TOP_RIGHT -> getAnchors().selectAnchor(ViewfinderAnchorPosition.TOP_LEFT);
                case MIDDLE_RIGHT -> getAnchors().selectAnchor(ViewfinderAnchorPosition.MIDDLE_LEFT);
                case BOTTOM_RIGHT -> getAnchors().selectAnchor(ViewfinderAnchorPosition.BOTTOM_LEFT);
            }
            getBoundingBox().setWidth(0);
        }

        if (getBoundingBox().getBottomMiddle().getY() < getBoundingBox().getTopMiddle().getY()) {
            getAnchors().deselectAllAnchors();
            switch(selectedAnchor) {
                case TOP_LEFT -> {
                    getAnchors().selectAnchor(ViewfinderAnchorPosition.BOTTOM_LEFT);
                    getBoundingBox().setTranslateY(getBoundingBox().getBottomMiddle().getY());
                }
                case TOP_MIDDLE -> {
                    getAnchors().selectAnchor(ViewfinderAnchorPosition.BOTTOM_MIDDLE);
                    getBoundingBox().setTranslateY(getBoundingBox().getBottomMiddle().getY());
                }
                case TOP_RIGHT -> {
                    getAnchors().selectAnchor(ViewfinderAnchorPosition.BOTTOM_RIGHT);
                    getBoundingBox().setTranslateY(getBoundingBox().getBottomMiddle().getY());
                }
                case BOTTOM_LEFT -> getAnchors().selectAnchor(ViewfinderAnchorPosition.TOP_LEFT);
                case BOTTOM_MIDDLE -> getAnchors().selectAnchor(ViewfinderAnchorPosition.TOP_MIDDLE);
                case BOTTOM_RIGHT -> getAnchors().selectAnchor(ViewfinderAnchorPosition.TOP_RIGHT);
            }
            getBoundingBox().setHeight(0);
        }
    }

    public void enterFullScreen() {
        boundingBox.setWidth(Monitor.getMonitorWidth());
        boundingBox.setHeight(Monitor.getMonitorHeight());
        boundingBox.setTranslateX(0);
        boundingBox.setTranslateY(0);
    }

    public boolean isFullScreen() {
        return (boundingBox.getWidth() == Monitor.getMonitorWidth()
                && boundingBox.getHeight() == Monitor.getMonitorHeight());
    }

    public Stage getStage() {
        return (Stage) getBoundingBox().getScene().getWindow();
    }

    public void allowMovement(boolean b) {
        isMovementAllowed = b;
    }

    public boolean getMovementAllowed() {
        return isMovementAllowed;
    }

    public void allowMousePassthrough(boolean b) {
        isMousePassthroughOn = b;

        if (b) {
            getStage().getScene().setFill(new Color(0.0, 0.0, 0.0, 0.00));
        } else {
            getStage().getScene().setFill(new Color(0.0, 0.0, 0.0, 0.01));
            getStage().setAlwaysOnTop(true); // sets viewfinder to top
        }
    }

    public void hideStage() {
        getWidgetController().getDrawingToolBar().setWidgetsDrawingStatus(false);
        getWidgetController().getDrawingToolBar().setWidgetsSelected(false);
        getWidgetController().getDrawData().deleteAllData();
        setVisible(false);
        getStage().hide();
    }

    public boolean isMousePassthroughAllowed() {
        return isMousePassthroughOn;
    }

    public ProgramTray getProgramTray() {
        return programTray;
    }
}

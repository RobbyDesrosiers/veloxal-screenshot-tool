/**
 * ViewfinderController.java
 * @Description: Encapsulates all separate, individual viewfinder elements
 */

package com.desrosiersrobby.veloxal.controllers;

import com.desrosiersrobby.veloxal.ui.ViewfinderDimensions;
import com.desrosiersrobby.veloxal.utilities.*;
import com.desrosiersrobby.veloxal.ui.ViewfinderBoundingBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ViewfinderController {
    // The main rectangle of the viewfinder
    private final ViewfinderBoundingBox boundingBox;
    // The 8 anchors located at all 4 corners and middle of each side
    private final ViewfinderAnchorList anchors;
    // The dimensions located in top left corner of the viewfinder
    private final ViewfinderDimensions dimensions;
    // The dimmed area located around the boundingBox
    private final ViewfinderNegativeSpaceList negativeSpace;
    // The controller for the toolbars/widget bars located on the bottom right/left sides
    private final WidgetBarController widgetController;
    // All display elements of the viewfinder. Used for easy screen rendering of elements
    private final ObservableList<DisplayElement> viewFinderElements = FXCollections.observableArrayList();
    // The program tray icon and functionality
    private final ProgramTray programTray;
    private boolean isCreated;
    private boolean isMovementAllowed;
    private boolean isMousePassthroughOn;

    public ViewfinderController(ProgramTray programTray) {
        // inits all viewfinder elements
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
        viewFinderElements.add(widgetController.getDrawingToolBar().getWidgetBarStrokeWidth());
        viewFinderElements.add(widgetController.getCommandToolBar());
        setCreated(false);  // elements are created but not rendered onto screen
        allowMovement(true);
    }

    /**
     * Creates and initializes elements of the viewfinder for screen render
     * @param mouseEvent
     */
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

    /**
     * Updates all associative elements of viewfinder positions
     */
    public void update() {
        getBoundingBox().update();
        getAnchors().update();
        getDimensions().update();
        getNegativeSpace().update();
        getWidgetController().update();
    }

    /**
     * Sets the visibility of all viewfinder elements
     * @param b: Boolean. True for visible, False for invisible
     */
    public void setVisible(boolean b) {
        setVisibilityForScreenshot(b);
        getNegativeSpace().setVisible(b);
    }

    /**
     * Same functionality as setVisible except does not set NegativeSpace
     * @param b
     */
    public void setVisibilityForScreenshot(boolean b) {
        getAnchors().setVisible(b);
        getBoundingBox().setVisible(b);
        getDimensions().setVisible(b);
        getWidgetController().getDrawingToolBar().setVisible(b);
        getWidgetController().getDrawingToolBar().getWidgetBarStrokeWidth().setVisible(b);
        getWidgetController().getCommandToolBar().setVisible(b);
    }

    /**
     * Quick way to access Bounding Box's setDragPoint function
     * @param mouseEvent
     */
    public void setDragPoint(MouseEvent mouseEvent) {
        getBoundingBox().setDragPoint(mouseEvent);
    }

    /**
     * Quick way to access Bounding Box's move function
     * @param mouseEvent
     */
    public void move(MouseEvent mouseEvent) {
        getBoundingBox().move(mouseEvent);
    }

    /**
     * Checks to see if user drags the viewfinder across it's opposite side. Without this function, any rectangles with
     * a negative width, height would not be rendered. This function checks what anchor is selected when the viewfinder
     * crosses it's opposite side then swiftly switches the selected anchor. This gives the appearance that the viewfinder
     * crossed itself
     */
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

    /**
     * Sets the viewfinder to monitor's width and height
     */
    public void enterFullScreen() {
        boundingBox.setWidth(Monitor.getMonitorWidth());
        boundingBox.setHeight(Monitor.getMonitorHeight());
        boundingBox.setTranslateX(0);
        boundingBox.setTranslateY(0);
    }

    /**
     *
     * @return True if viewfinder is equal to the monitor's height and width
     *         False if not
     */
    public boolean isFullScreen() {
        return (boundingBox.getWidth() == Monitor.getMonitorWidth()
                && boundingBox.getHeight() == Monitor.getMonitorHeight());
    }

    /**
     * Sets the isMovementAllowed variable. Used to check if user has ability to move the viewfinder
     * @param b
     */
    public void allowMovement(boolean b) {
        isMovementAllowed = b;
    }

    public boolean getMovementAllowed() {
        return isMovementAllowed;
    }

    /**
     * Allows window pane to become completely transparent allowing mouse pass through to objects underneath it
     * This will allow user to select elements on their screen with the window pane on top of other windows
     * @param b
     */
    public void allowMousePassthrough(boolean b) {
        isMousePassthroughOn = b;

        if (b) {
            getStage().getScene().setFill(new Color(0.0, 0.0, 0.0, 0.00));
        } else {
            getStage().getScene().setFill(new Color(0.0, 0.0, 0.0, 0.01));
            getStage().setAlwaysOnTop(true); // sets viewfinder to top
        }
    }

    /**
     * Hides the entire stage from user, essentially making the program disappear
     */
    public void hideStage() {
        getWidgetController().getDrawingToolBar().setWidgetsDrawingStatus(false);
        getWidgetController().getDrawingToolBar().setWidgetsSelected(false);
        getWidgetController().getDrawData().deleteAllData();
        setVisible(false);
        getStage().hide();
    }

    public Stage getStage() {
        return (Stage) getBoundingBox().getScene().getWindow();
    }

    public boolean isMousePassthroughAllowed() {
        return isMousePassthroughOn;
    }

    public ProgramTray getProgramTray() {
        return programTray;
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
}

package com.quickshot.quickshot;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.CacheHint;
import javafx.scene.input.MouseEvent;

public class Viewfinder implements DisplayElement {
    // Viewfinder components
    private ViewfinderBoundingBox boundingBox;
    private ViewfinderAnchorList anchors;
    private ViewfinderDimensions dimensions;
    private ViewfinderNegativeSpaceList negativeSpace;
    private ViewfinderWidgetBar widgetBar;
    private final ObservableList<DisplayElement> viewFinderElements = FXCollections.observableArrayList();

    public Viewfinder() {
    }

    public Viewfinder(MouseEvent mouseEvent) {
        this();
        createViewfinder(mouseEvent);
    }

    public void createViewfinder(MouseEvent mouseEvent) {
        boundingBox = new ViewfinderBoundingBox(mouseEvent);
        anchors = new ViewfinderAnchorList(boundingBox);
        dimensions = new ViewfinderDimensions(boundingBox);
        negativeSpace = new ViewfinderNegativeSpaceList(boundingBox);
        widgetBar = new ViewfinderWidgetBar(boundingBox);

        //TODO check this on local windows machine
//        boundingBox.setCache(true);
//        boundingBox.setCacheHint(CacheHint.SPEED);
//        for (ViewfinderAnchor anchor : anchors) {
//            anchor.setCache(true);
//            anchor.setCacheHint(CacheHint.SPEED);
//        }
//        dimensions.setCache(true);
//        dimensions.setCacheHint(CacheHint.SPEED);
//        negativeSpace.forEach(rect -> {
//            rect.setCache(true);
//            rect.setCacheHint(CacheHint.SPEED);
//        });

        viewFinderElements.add(boundingBox);
        viewFinderElements.addAll(anchors);
        viewFinderElements.add(dimensions);
        viewFinderElements.addAll(negativeSpace);
        viewFinderElements.add(widgetBar);
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

    public boolean isSelected(MouseEvent mouseEvent) {
        return getBoundingBox().isMouseOver();
    }

    @Override
    public void update() {
        getBoundingBox().update();
        getAnchors().update();
        getDimensions().update();
        getNegativeSpace().update();
        getWidgetBar().update();
    }

    private ViewfinderWidgetBar getWidgetBar() {
        return widgetBar;
    }

    private ViewfinderNegativeSpaceList getNegativeSpace() {
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
}

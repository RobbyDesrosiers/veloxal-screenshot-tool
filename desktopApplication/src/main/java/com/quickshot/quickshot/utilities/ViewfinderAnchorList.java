/**
 * ViewfinderAnchorList.java
 * @Description: Used to generate and encapsulate the 8 anchors that compose the viewfinder for easy manupulation
 */

package com.quickshot.quickshot.utilities;

import com.quickshot.quickshot.ui.ViewfinderAnchor;
import com.quickshot.quickshot.ui.ViewfinderBoundingBox;
import javafx.scene.Cursor;
import java.util.ArrayList;

public class ViewfinderAnchorList extends ArrayList<ViewfinderAnchor> implements DisplayElement {
    // uses boundingBox as a reference to place anchors properly
    private ViewfinderBoundingBox boundingBox;

    /**
     * Generates the 8 anchors and stores them inside of the extended Arraylist
     */
    public ViewfinderAnchorList() {
        clear();
        for (int i = 0; i < 8; i++) {
            ViewfinderAnchor a = new ViewfinderAnchor(i);
            add(a);
        }
    }

    public ViewfinderAnchorList(ViewfinderBoundingBox boundingBox) {
        this();
        setBoundingBox(boundingBox);
    }

    public void setBoundingBox(ViewfinderBoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    /**
     * Calculates anchor positions on screenRefresh (this function used in update() function) to calc the
     * coordinates where the anchors need to be rendered onto the screen
     */
    private void calculateAnchorPositions() {
        for (ViewfinderAnchor anchor : this) {
            switch (anchor.getAnchorPosition()) {
                case TOP_LEFT -> {
                    anchor.setCoordinates(boundingBox.getTopLeft());
                }
                case TOP_MIDDLE -> {
                    anchor.setCoordinates(boundingBox.getTopMiddle());
                }
                case TOP_RIGHT -> {
                    anchor.setCoordinates(boundingBox.getTopRight());
                }
                case MIDDLE_LEFT -> {
                    anchor.setCoordinates(boundingBox.getMiddleLeft());
                }
                case MIDDLE_RIGHT -> {
                    anchor.setCoordinates(boundingBox.getMiddleRight());
                }
                case BOTTOM_LEFT -> {
                    anchor.setCoordinates(boundingBox.getBottomLeft());
                }
                case BOTTOM_MIDDLE -> {
                    anchor.setCoordinates(boundingBox.getBottomMiddle());
                }
                case BOTTOM_RIGHT -> {
                    anchor.setCoordinates(boundingBox.getBottomRight());
                }
                default -> throw new IndexOutOfBoundsException("Too many anchors created");
            }
        }
    }

    /**
     *
     * @param anchorPosition: The position of the anchor needing to be returned
     * @return: The anchor which matches the anchorPosition passed in
     */
    public ViewfinderAnchor getAnchor(ViewfinderAnchorPosition anchorPosition) {
        for (ViewfinderAnchor anchor : this) {
            if (anchor.getAnchorPosition() == anchorPosition) {
                return anchor;
            }
        }
        return null;
    }

    /**
     * Gets the type of cursor based on which anchor is hovered over (used for UserController)
     * @return: The anchor cursor type
     */
    public Cursor getHoveredAnchorCursorType() {
        for (ViewfinderAnchor anchor : this) {
            if (anchor.isMouseOver())
                return anchor.getCursorType();
        }
        return null;
    }

    public boolean isMouseOver() {
        for (ViewfinderAnchor anchor : this) {
            if (anchor.isMouseOver())
                return true;
        }
        return false;
    }

    public ViewfinderAnchorPosition getSelectedAnchorPosition() {
        for (ViewfinderAnchor anchor : this) {
            if (anchor.isSelected()) {
                return anchor.getAnchorPosition();
            }
        }
        return null;
    }

    public boolean isSelected() {
        for (ViewfinderAnchor anchor : this) {
            if (anchor.isSelected()) {
                return true;
            }
        }
        return false;
    }

    public void deselectAllAnchors() {
        for (ViewfinderAnchor anchor : this) {
            anchor.setSelected(false);
        }
    }

    @Override
    public void update() {
        calculateAnchorPositions();
    }

    /**
     * sets the anchor needing to be selected to true. Primarily used in the ViewfinderController.java class
     * @param anchorPosition
     */
    public void selectAnchor(ViewfinderAnchorPosition anchorPosition) {
        for (ViewfinderAnchor anchor : this) {
            if (anchor.getAnchorPosition() == anchorPosition) {
                anchor.setSelected(true);
            }
        }
    }

    public void setVisible(boolean b) {
        for (ViewfinderAnchor anchor : this) {
            anchor.setVisible(b);
        }
    }
}

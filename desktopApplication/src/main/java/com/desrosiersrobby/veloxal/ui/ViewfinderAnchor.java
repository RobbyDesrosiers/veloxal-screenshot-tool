/**
 * ViewfinderAnchor.java
 * @Description: The graphical element for anchors, located on 4 corners and 4 sides of the rectangle
 */
package com.desrosiersrobby.veloxal.ui;

import com.desrosiersrobby.veloxal.utilities.Coordinate;
import com.desrosiersrobby.veloxal.utilities.DisplayElement;
import com.desrosiersrobby.veloxal.utilities.ViewfinderAnchorPosition;
import com.desrosiersrobby.veloxal.utilities.ViewfinderViewOrder;
import javafx.scene.CacheHint;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ViewfinderAnchor extends Rectangle implements DisplayElement {
    private ViewfinderAnchorPosition anchorPosition;
    private Cursor cursorType;
    private final int WIDTH = 5;
    private final int HEIGHT = WIDTH;

    private boolean isMouseOver = false;
    private boolean isSelected = false;

    public ViewfinderAnchor(int i) {
        setWidth(WIDTH);
        setHeight(HEIGHT);
        setStroke(Color.WHITE);
        setFill(Color.BLACK);
        setViewOrder(ViewfinderViewOrder.ANCHORS);
        setCache(true);
        setCacheHint(CacheHint.SPEED);
        setAnchorTraits(i);
        initHoverProperties();
    }

    /**
     * Initializes all hover mouse events for the anchor. This code runs first before anything in UserController.java
     */
    public void initHoverProperties() {
        this.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> setMouseOver(true));
        this.addEventFilter(MouseEvent.MOUSE_EXITED, e -> setMouseOver(false));
        this.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> setSelected(true));
        this.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> setSelected(false));
    }

    private void setMouseOver(boolean b) {
        isMouseOver = b;
    }

    /**
     * The setup function of each anchor to correctly define the AnchorPosition and type of cursor which is used on hover
     * @param index: Index from ViewFinderAnchorList's generator
     */
    public void setAnchorTraits(int index) {
        switch (index) {
            case 0: {
                anchorPosition = ViewfinderAnchorPosition.TOP_LEFT;
                cursorType = Cursor.NW_RESIZE;
                break;
            }
            case 1: {
                anchorPosition = ViewfinderAnchorPosition.TOP_MIDDLE;
                cursorType = Cursor.N_RESIZE;
                break;
            }
            case 2: {
                anchorPosition = ViewfinderAnchorPosition.TOP_RIGHT;
                cursorType = Cursor.NE_RESIZE;
                break;
            }
            case 3: {
                anchorPosition = ViewfinderAnchorPosition.MIDDLE_LEFT;
                cursorType = Cursor.W_RESIZE;
                break;
            }
            case 4: {
                anchorPosition = ViewfinderAnchorPosition.MIDDLE_RIGHT;
                cursorType = Cursor.E_RESIZE;
                break;
            }
            case 5: {
                anchorPosition = ViewfinderAnchorPosition.BOTTOM_LEFT;
                cursorType = Cursor.SW_RESIZE;
                break;
            }
            case 6: {
                anchorPosition = ViewfinderAnchorPosition.BOTTOM_MIDDLE;
                cursorType = Cursor.S_RESIZE;
                break;
            }
            case 7: {
                anchorPosition = ViewfinderAnchorPosition.BOTTOM_RIGHT;
                cursorType = Cursor.SE_RESIZE;
                setSelected(true);
                break;
            }
            default: { throw new IndexOutOfBoundsException("Too many anchors created"); }
        }
    }

    /**
     * Sets the screen position of the anchor
     * @param coordinate: The new screen position
     */
    public void setCoordinates(Coordinate coordinate) {
        this.setTranslateX(coordinate.getX() + -0.5 * WIDTH);
        this.setTranslateY(coordinate.getY() + -0.5 * HEIGHT);
    }

    public ViewfinderAnchorPosition getAnchorPosition() {
        return anchorPosition;
    }

    public void setAnchorPosition(ViewfinderAnchorPosition anchorPosition) {
        this.anchorPosition = anchorPosition;
    }

    public Cursor getCursorType() {
        return cursorType;
    }

    public boolean isMouseOver() {
        return isMouseOver;
    }

    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void update() {

    }

    public void setSelected(boolean b) {
        isSelected = b;
    }
}

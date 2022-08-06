/**
 * ViewfinderBoundingBox.java
 * @Desciption: The rectangle which forms the basis of the viewfinder. This holds Coordinates to assist other
 * DisplayElements in the viewfinder by keeping up-to-date Coordinate (points) for each corner of the rect.
 * These coordinates are used throughout the program to calculate other coordinate locations of graphical elements.
 * ViewfinderBoundingBox otherwise is a rectangle
 *
 *      Coordinate Map
 *      A ---------- B
 *      |            |
 *      |            |
 *      C ---------- D
 */
package com.desrosiersrobby.veloxal.ui;

import com.desrosiersrobby.veloxal.utilities.BoundingBoxSnap;
import com.desrosiersrobby.veloxal.utilities.Coordinate;
import com.desrosiersrobby.veloxal.utilities.DisplayElement;
import com.desrosiersrobby.veloxal.utilities.ViewfinderViewOrder;
import javafx.scene.CacheHint;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ViewfinderBoundingBox extends Rectangle implements DisplayElement {
    private final Coordinate pointA = new Coordinate();
    private final Coordinate pointB = new Coordinate();
    private final Coordinate pointC = new Coordinate();
    private final Coordinate pointD = new Coordinate();
    private final Coordinate clickedStartingPoint = new Coordinate();
    private final BoundingBoxSnap boundingBoxSnap;
    private boolean isMouseOver;
    private boolean isSelected;

    public ViewfinderBoundingBox() {
        super(0, 0);
        boundingBoxSnap = new BoundingBoxSnap(true);

        setFill(Color.TRANSPARENT);
        getStrokeDashArray().addAll(4d);
        setStroke(Color.WHITE);
        setCache(true);
        setViewOrder(ViewfinderViewOrder.BOUNDING_BOX);
        setCacheHint(CacheHint.SPEED);
        initMouseEvents();
        initHoverProperties();
        update();
    }

    /**
     * Used when the user's mouse clicks on the background of ScreenOverlay. This sets the viewfinder to a
     * 0,0 height/width and the x/y coodinates to where the mouse click happened essentially resetting
     * the view of the viewfinder
     * @param mouseEvent: where the mouse clicks on the ScreenOverlay background
     */
    public void resetViewfinderBoundingBox(MouseEvent mouseEvent) {
        setTranslateX(mouseEvent.getSceneX());
        setTranslateY(mouseEvent.getSceneY());
        setHeight(0);
        setWidth(0);
    }

    /**
     * Initializes the hover properties of the mouse. This will run before any code in UserController
     */
    public void initHoverProperties() {
        this.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> isMouseOver = true);
        this.addEventFilter(MouseEvent.MOUSE_EXITED, e -> isMouseOver = false);
    }

    /**
     * Initializes the clicked properties of the mouse. This will run before any code in UserController
     */
    private void initMouseEvents() {
        setOnMousePressed(e -> setSelected(true));
        setOnMouseReleased(e -> setSelected(false));
    }

    /**
     * When dragged, this sets the initial point where the mouse clicks on the rectangle. It calculates the coordinates
     * of the offset of the mouse to the top left corner of the rectangle. This allows the drag function to properly
     * calculate the position of the rectangle when dragged with relation to the mouse
     * @param mouseEvent: Where the mouse clicks on the rectangle
     */
    public void setDragPoint(MouseEvent mouseEvent) {
        clickedStartingPoint.setX(this.getTranslateX() - mouseEvent.getSceneX());
        clickedStartingPoint.setY(this.getTranslateY() - mouseEvent.getSceneY());
    }

    /**
     * Moves the rectangle in relation to the mouse's movements so the user can drag/pan the rectangle
     * @param mouseEvent: The new coordinates of the mouse
     */
    public void move(MouseEvent mouseEvent) {
        setTranslateX(mouseEvent.getSceneX() + clickedStartingPoint.getX());
        setTranslateY(mouseEvent.getSceneY() + clickedStartingPoint.getY());
    }

    /**
     * Moves only the right side of the rectangle which allows the user to scale the width of the rectangle
     * @param mouseEvent: the new coordinates of the mouse
     */
    public void moveRight(MouseEvent mouseEvent) {
        this.setWidth(mouseEvent.getSceneX() - this.getTranslateX());
    }

    /**
     * Moves only the bottom side of the rectangle which allows the user to scale the width of the rectangle
     * @param mouseEvent: the new coordinates of the mouse
     */
    public void moveDown(MouseEvent mouseEvent) {
        this.setHeight(mouseEvent.getSceneY() - this.getTranslateY());
    }

    /**
     * Moves only the top side of the rectangle which allows the user to scale the width of the rectangle
     * @param mouseEvent: the new coordinates of the mouse
     */
    public void moveUp(MouseEvent mouseEvent) {
        double oldTranslateY = this.getTranslateY();
        this.setTranslateY(mouseEvent.getSceneY());
        this.setHeight(this.getHeight() - (this.getTranslateY() - oldTranslateY));
    }

    /**
     * Moves only the left side of the rectangle which allows the user to scale the width of the rectangle
     * @param mouseEvent: the new coordinates of the mouse
     */
    public void moveLeft(MouseEvent mouseEvent) {
        double oldTranslateX = this.getTranslateX();
        this.setTranslateX(mouseEvent.getSceneX());
        this.setWidth(this.getWidth() - (this.getTranslateX() - oldTranslateX));
    }

    /**
     * Allows easy functionality to move in a diagonal direction. Used primarily when user is selecting a corner anchor
     * @param mouseEvent: the new coordinates of the mouse
     */
    public void moveDownRight(MouseEvent mouseEvent) {
        this.moveDown(mouseEvent);
        this.moveRight(mouseEvent);
    }

    /**
     * Allows easy functionality to move in a diagonal direction. Used primarily when user is selecting a corner anchor
     * @param mouseEvent: the new coordinates of the mouse
     */
    public void moveDownLeft(MouseEvent mouseEvent) {
        this.moveDown(mouseEvent);
        this.moveLeft(mouseEvent);
    }

    /**
     * Allows easy functionality to move in a diagonal direction. Used primarily when user is selecting a corner anchor
     * @param mouseEvent: the new coordinates of the mouse
     */
    public void moveUpRight(MouseEvent mouseEvent) {
        this.moveUp(mouseEvent);
        this.moveRight(mouseEvent);
    }

    /**
     * Allows easy functionality to move in a diagonal direction. Used primarily when user is selecting a corner anchor
     * @param mouseEvent: the new coordinates of the mouse
     */
    public void moveUpLeft(MouseEvent mouseEvent) {
        this.moveUp(mouseEvent);
        this.moveLeft(mouseEvent);
    }

    /**
     * @return: The top left coordinate (x,y position) of the BoundingBox
     */
    public Coordinate getTopLeft() {
        return pointA;
    }

    /**
     * @return: The top right coordinate (x,y position) of the BoundingBox
     */
    public Coordinate getTopRight() {
        return pointB;
    }

    /**
     * @return: The bottom left coordinate (x,y position) of the BoundingBox
     */
    public Coordinate getBottomLeft() {
        return pointC;
    }

    /**
     * @return: The bottom right coordinate (x,y position) of the BoundingBox
     */
    public Coordinate getBottomRight() {
        return pointD;
    }

    /**
     * @return: The top center coordinate (x,y position) of the BoundingBox
     */
    public Coordinate getTopMiddle() {
        return new Coordinate(pointA.getX() + (0.5 * (pointB.getX() - pointA.getX())), pointB.getY());
    }

    /**
     * @return: The middle right coordinate (x,y position) of the BoundingBox
     */
    public Coordinate getMiddleRight() {
        return new Coordinate(pointB.getX(), (pointB.getY() + (0.5 * (pointD.getY() - pointB.getY()))));
    }

    /**
     * @return: The bottom middle coordinate (x,y position) of the BoundingBox
     */
    public Coordinate getBottomMiddle() {
        return new Coordinate(pointC.getX() + (0.5 * (pointD.getX() - pointC.getX())), pointC.getY());
    }

    /**
     * @return: The middle left coordinate (x,y position) of the BoundingBox
     */
    public Coordinate getMiddleLeft() {
        return new Coordinate(pointA.getX(), pointB.getY() + (0.5 * (pointC.getY() - pointA.getY())));
    }

    public boolean isMouseOver() {
        return isMouseOver;
    }

    /**
     * Recalculates each coordinate of the bounding box on each update() function call
     */
    public void calculateBoundingBoxPoints() {
        double translateX = getTranslateX();
        double translateY = getTranslateY();
        double width = getWidth();
        double height = getHeight();
        pointA.setX(translateX);
        pointC.setX(translateX);
        pointB.setX(translateX + width);
        pointD.setX(translateX + width);
        pointA.setY(translateY);
        pointB.setY(translateY);
        pointC.setY(translateY + height);
        pointD.setY(translateY + height);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public BoundingBoxSnap getBoundingBoxSnap() {
        return boundingBoxSnap;
    }

    @Override
    public void update() {
        calculateBoundingBoxPoints();
    }



}

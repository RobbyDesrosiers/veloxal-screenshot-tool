package com.quickshot.quickshot.ui;

import com.quickshot.quickshot.utilities.ViewfinderViewOrder;
import com.quickshot.quickshot.utilities.Coordinate;
import com.quickshot.quickshot.utilities.DisplayElement;
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
    private boolean isMouseOver;
    private boolean isSelected;

    // A ---------- B
    // |            |
    // |            |
    // C ---------- D

    public ViewfinderBoundingBox() {
        super(0, 0);
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

    public ViewfinderBoundingBox(MouseEvent mouseEvent) {
        this();
        resetViewfinderBoundingBox(mouseEvent);
    }

    public void resetViewfinderBoundingBox(MouseEvent mouseEvent) {
        setTranslateX(mouseEvent.getSceneX());
        setTranslateY(mouseEvent.getSceneY());
        setHeight(0);
        setWidth(0);
    }

    public void initHoverProperties() {
        this.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> isMouseOver = true);
        this.addEventFilter(MouseEvent.MOUSE_EXITED, e -> isMouseOver = false);
    }

    private void initMouseEvents() {
        setOnMousePressed(e -> setSelected(true));
        setOnMouseReleased(e -> setSelected(false));
    }

    public void setDragPoint(MouseEvent mouseEvent) {
        clickedStartingPoint.setX(this.getTranslateX() - mouseEvent.getSceneX());
        clickedStartingPoint.setY(this.getTranslateY() - mouseEvent.getSceneY());
    }

    public void move(MouseEvent mouseEvent) {
        setTranslateX(mouseEvent.getSceneX() + clickedStartingPoint.getX());
        setTranslateY(mouseEvent.getSceneY() + clickedStartingPoint.getY());
    }

    public void moveDownRight(MouseEvent mouseEvent) {
        this.moveDown(mouseEvent);
        this.moveRight(mouseEvent);
    }

    public void moveDownLeft(MouseEvent mouseEvent) {
        this.moveDown(mouseEvent);
        this.moveLeft(mouseEvent);
    }

    public void moveUpRight(MouseEvent mouseEvent) {
        this.moveUp(mouseEvent);
        this.moveRight(mouseEvent);
    }

    public void moveUpLeft(MouseEvent mouseEvent) {
        this.moveUp(mouseEvent);
        this.moveLeft(mouseEvent);
    }

    public void moveRight(MouseEvent mouseEvent) {
        this.setWidth(mouseEvent.getSceneX() - this.getTranslateX());
    }

    public void moveDown(MouseEvent mouseEvent) {
        this.setHeight(mouseEvent.getSceneY() - this.getTranslateY());
    }

    public void moveUp(MouseEvent mouseEvent) {
        double oldTranslateY = this.getTranslateY();
        this.setTranslateY(mouseEvent.getSceneY());
        this.setHeight(this.getHeight() - (this.getTranslateY() - oldTranslateY));
    }

    public void moveLeft(MouseEvent mouseEvent) {
        double oldTranslateX = this.getTranslateX();
        this.setTranslateX(mouseEvent.getSceneX());
        this.setWidth(this.getWidth() - (this.getTranslateX() - oldTranslateX));
    }

    public Coordinate getTopLeft() {
        return pointA;
    }

    public Coordinate getTopRight() {
        return pointB;
    }

    public Coordinate getBottomLeft() {
        return pointC;
    }

    public Coordinate getBottomRight() {
        return pointD;
    }

    public Coordinate getTopMiddle() {
        return new Coordinate(pointA.getX() + (0.5 * (pointB.getX() - pointA.getX())), pointB.getY());
    }

    public Coordinate getMiddleRight() {
        return new Coordinate(pointB.getX(), (pointB.getY() + (0.5 * (pointD.getY() - pointB.getY()))));
    }

    public Coordinate getBottomMiddle() {
        return new Coordinate(pointC.getX() + (0.5 * (pointD.getX() - pointC.getX())), pointC.getY());
    }

    public Coordinate getMiddleLeft() {
        return new Coordinate(pointA.getX(), pointB.getY() + (0.5 * (pointC.getY() - pointA.getY())));
    }

    public boolean isMouseOver() {
        return isMouseOver;
    }

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

    @Override
    public void update() {
        calculateBoundingBoxPoints();
    }



}

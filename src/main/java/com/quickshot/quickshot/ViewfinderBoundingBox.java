package com.quickshot.quickshot;

import javafx.scene.CacheHint;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ViewfinderBoundingBox extends Rectangle implements DisplayElement {
    private Coordinate pointA = new Coordinate();
    private Coordinate pointB = new Coordinate();
    private Coordinate pointC = new Coordinate();
    private Coordinate pointD = new Coordinate();
    private Coordinate clickedStartingPoint = new Coordinate();
    private boolean isMouseOver;

    // A ---------- B
    // |            |
    // |            |
    // C ---------- D

    public ViewfinderBoundingBox(MouseEvent mouseEvent) {
        super(0, 0);
        setTranslateX(mouseEvent.getSceneX());
        setTranslateY(mouseEvent.getSceneY());
        setFill(Color.TRANSPARENT);
        getStrokeDashArray().addAll(2d);
        setStroke(Color.GRAY);
        setCache(true);
        setCacheHint(CacheHint.SPEED);
        initHoverProperties();
        update();
    }

    public void initHoverProperties() {
        this.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> isMouseOver = true);
        this.addEventFilter(MouseEvent.MOUSE_EXITED, e -> isMouseOver = false);
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


    @Override
    public void update() {
        calculateBoundingBoxPoints();
    }



}

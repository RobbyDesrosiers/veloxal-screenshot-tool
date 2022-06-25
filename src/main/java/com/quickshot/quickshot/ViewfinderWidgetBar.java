package com.quickshot.quickshot;

import javafx.scene.Node;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;

public class ViewfinderWidgetBar extends VBox implements DisplayElement {
    private ViewfinderBoundingBox boundingBox;
    private final ToolBar toolbar;
    private WidgetDrawData drawData;

    public ViewfinderWidgetBar() {
        toolbar = new ToolBar();
        drawData = new WidgetDrawData();
        addWidgetToToolbar(new WidgetPaintbrush("paint-brush.png", drawData));
        addWidgetToToolbar(new WidgetHighlighter("marker.png", drawData));
        getChildren().add(toolbar);
        setViewOrder(-1);
    }

    public ViewfinderWidgetBar(ViewfinderBoundingBox boundingBox) {
        this();
        setBoundingBox(boundingBox);
    }

    private void addWidgetToToolbar(Widget widget) {
        toolbar.getItems().add(widget);
    }

    private void calculateScreenPosition() {
        int OFFSET = 10;
        double Y_PADDING;
        double X_PADDING;

        if (boundingBox.getBottomMiddle().getY() + getHeight() + OFFSET > Monitor.getMonitorHeight()) {
            Y_PADDING = -getHeight() - 5;
            X_PADDING = -5;
        } else {
            Y_PADDING = 5;
            X_PADDING = 0;
        }

        setTranslateX(boundingBox.getBottomRight().getX() - getWidth() + X_PADDING);
        setTranslateY(boundingBox.getBottomMiddle().getY() + Y_PADDING);
        setTranslateZ(-1);
    }

    public WidgetDrawData getDrawData() {
        return drawData;
    }

    public void setDrawData(WidgetDrawData drawData) {
        this.drawData = drawData;
    }

    @Override
    public void update() {
        calculateScreenPosition();
    }

    public boolean isWidgetSelected() {
        Widget widget;
        for (Node node : toolbar.getItems()) {
            widget = (Widget)node;
            if (widget.isSelected()) {
                return true;
            }
        }
        return false;
    }

    public Widget getSelectedWidget() {
        Widget widget;
        for (Node node : toolbar.getItems()) {
            widget = (Widget)node;
            if (widget.isSelected()) {
                return widget;
            }
        }
        return null;
    }

    public void setBoundingBox(ViewfinderBoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }
}

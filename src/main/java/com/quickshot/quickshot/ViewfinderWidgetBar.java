package com.quickshot.quickshot;

import javafx.scene.Node;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;

public class ViewfinderWidgetBar extends VBox implements DisplayElement {
    private final ViewfinderBoundingBox boundingBox;
    private final ToolBar toolbar;
    private WidgetDrawData drawData;

    public ViewfinderWidgetBar(ViewfinderBoundingBox boundingBox) {
        this.boundingBox = boundingBox;
        toolbar = new ToolBar();
        drawData = new WidgetDrawData();
        addWidgetToToolbar(new WidgetPaintbrush("paint-brush.png", drawData));
        setSpacing(15);
        getChildren().add(toolbar);
    }

    private void addWidgetToToolbar(Widget widget) {
        toolbar.getItems().add(widget);
    }

    private void calculateScreenPosition() {
        double Y_PADDING = 5;
        setTranslateX(boundingBox.getBottomRight().getX() - getWidth());
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
}

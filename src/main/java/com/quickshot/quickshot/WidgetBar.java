package com.quickshot.quickshot;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public abstract class WidgetBar extends Pane {
    private final ToolBar toolBar;
    private final ArrayList<Widget> widgets;
    private final ViewfinderBoundingBox boundingBox;
    private final WidgetDrawData drawData;
    private boolean isMouseOver;

    public WidgetBar(ViewfinderBoundingBox boundingBox,WidgetDrawData drawData) {
        widgets = new ArrayList<>();
        toolBar = new ToolBar();
        this.boundingBox = boundingBox;
        this.drawData = drawData;
        getChildren().add(getToolBar());
        setVisible(false);
        getStyleClass().add("widget-bar");

        getToolBar().setViewOrder(ViewfinderViewOrder.WIDGET_BAR);
        getToolBar().getStyleClass().add("widget-bar");
        getToolBar().setPadding(new Insets(3,3,3,3));
        initMouseEvents();
    }

    private void initMouseEvents() {
        setOnMouseEntered(e -> setMouseOver(true));
        setOnMouseExited(e -> setMouseOver(false));
    }

    public void addWidget(Widget w) {
        getToolBar().getItems().add(w);
        getWidgets().add(w);
        w.setOnMouseClicked(this::handleWidgetClicked);
    }

    private void handleWidgetClicked(MouseEvent mouseEvent) {
        Widget selectedWidget = (Widget) mouseEvent.getTarget();

        // deselects widget to stop using it
        if (selectedWidget.isSelected()) {
            selectedWidget.setSelected(false);
        } else {
            selectedWidget.setSelected(true);
            // deselects all widgets besides newly selected
            for (Widget widget : getWidgets()) {
                if (!widget.equals(selectedWidget))
                    widget.setSelected(false);
            }
        }
    }

    public boolean isWidgetSelected() {
        Widget widget;
        for (Node node : getToolBar().getItems()) {
            widget = (Widget)node;
            if (widget.isSelected()) {
                return true;
            }
        }
        return false;
    }

    public Widget getSelectedWidget() {
        Widget widget;
        for (Node node : getToolBar().getItems()) {
            widget = (Widget)node;
            if (widget.isSelected()) {
                return widget;
            }
        }
        return null;
    }

    public ToolBar getToolBar() {
        return toolBar;
    }

    public ArrayList<Widget> getWidgets() {
        return widgets;
    }

    public WidgetDrawData getDrawData() {
        return drawData;
    }

    public void setOrientation(Orientation o) {
        getToolBar().setOrientation(o);
    }

    public boolean isMouseOver() {
        return isMouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        isMouseOver = mouseOver;
    }

    public ViewfinderBoundingBox getBoundingBox() {
        return boundingBox;
    }

}

package com.quickshot.quickshot.ui.abstracts;

import com.quickshot.quickshot.ui.Widget;
import com.quickshot.quickshot.utilities.WidgetDrawData;
import com.quickshot.quickshot.ui.ViewfinderBoundingBox;
import com.quickshot.quickshot.utilities.ViewfinderViewOrder;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.awt.*;
import java.util.ArrayList;

public abstract class WidgetBar extends TilePane {
    private final ArrayList<Widget> widgets;
    private final ViewfinderBoundingBox boundingBox;
    private final WidgetDrawData drawData;
    private boolean isMouseOver;

    public WidgetBar(ViewfinderBoundingBox boundingBox,WidgetDrawData drawData) {
        widgets = new ArrayList<>();
        this.boundingBox = boundingBox;
        this.drawData = drawData;
//        setSpacing(2);
        setVisible(false);
        setPadding(new Insets(1,1,1,1));
        getStyleClass().add("widget-bar");
        initMouseEvents();
    }

    private void initMouseEvents() {
        setOnMouseEntered(e -> setMouseOver(true));
        setOnMouseExited(e -> setMouseOver(false));
    }

    public void addToggleWidget(Widget w) {
        getChildren().add(w);
        getWidgets().add(w);
        w.setOnMouseClicked(this::handleWidgetClicked);
    }

    public void addWidget(Widget w) {
        getChildren().add(w);
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
        for (Widget widget : getWidgets()) {
            if (widget.isSelected()) {
                return true;
            }
        }
        return false;
    }

    public Widget getSelectedWidget() {
        for (Widget widget : getWidgets()) {
            if (widget.isSelected()) {
                return widget;
            }
        }
        return null;
    }

    public ArrayList<Widget> getWidgets() {
        return widgets;
    }

    public WidgetDrawData getDrawData() {
        return drawData;
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

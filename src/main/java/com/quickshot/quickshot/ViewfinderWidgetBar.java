package com.quickshot.quickshot;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class ViewfinderWidgetBar extends HBox implements DisplayElement {
    private ViewfinderBoundingBox boundingBox;
    private final ToolBar toolbar;
    private WidgetDrawData drawData;
    private boolean isMouseOver;
    private final WidgetUndo undoButton;
    private final WidgetClose closeButton;
    private final ArrayList<Widget> widgets;

    public ViewfinderWidgetBar() {
        toolbar = new ToolBar();
        drawData = new WidgetDrawData();
        widgets = new ArrayList<>();

        // create widgets
        undoButton = new WidgetUndo("undo.png", drawData);
        closeButton = new WidgetClose("close.png");
        addWidget(new WidgetPaintbrush("paint-brush.png", drawData));
        addWidget(new WidgetHighlighter("marker.png", drawData));
        toolbar.getItems().add(undoButton);
        toolbar.getItems().add(closeButton);
        getChildren().add(toolbar);

        // settings
        setViewOrder(-1);
        setVisible(false);
        toolbar.setPadding(new Insets(3,3,3,3));
        initMouseEvents();
    }

    public ViewfinderWidgetBar(ViewfinderBoundingBox boundingBox) {
        this();
        setBoundingBox(boundingBox);
    }

    private void initMouseEvents() {
        setOnMouseEntered(e -> setMouseOver(true));
        setOnMouseExited(e -> setMouseOver(false));

        // undo button is a 1 press event, does not need to be selected. This function ensures the currently selected
        // widget stays selected even when pressing the undo button
        undoButton.setOnMouseReleased(e -> handleUndoButton());
        closeButton.setOnMouseClicked(e -> Platform.exit());
    }

    private void addWidget(Widget w) {
        toolbar.getItems().add(w);
        widgets.add(w);
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
            for (Widget widget : widgets) {
                if (!widget.equals(selectedWidget))
                    widget.setSelected(false);
            }
        }
    }

    private void handleUndoButton() {
        Widget selectedWidget = getSelectedWidget();
        undoButton.undo();
        if (selectedWidget != null)
            selectedWidget.setSelected(true);
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
        setViewOrder(-1);
    }

    public WidgetDrawData getDrawData() {
        return drawData;
    }

    public void setDrawData(WidgetDrawData drawData) {
        this.drawData = drawData;
    }

    public void setBoundingBox(ViewfinderBoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public Widget getUndoButton() {
        return undoButton;
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

    public boolean isMouseOver() {
        return isMouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        isMouseOver = mouseOver;
    }

    public ArrayList<Widget> getWidgets() {
        return widgets;
    }
}

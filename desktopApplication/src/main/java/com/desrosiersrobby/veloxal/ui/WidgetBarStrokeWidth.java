/**
 * WidgetBarStrokeWidth.java
 * @Description: Visual element that spawns when a drawing tool is selected in order to allow user adjustment of their
 * thickness of stroke for the tool being used
 */

package com.desrosiersrobby.veloxal.ui;

import com.desrosiersrobby.veloxal.utilities.DisplayElement;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;

public class WidgetBarStrokeWidth extends TilePane implements DisplayElement {
    private boolean isMouseOver;
    Button btnPlus;
    Button btnMinus;
    Text width;

    public WidgetBarStrokeWidth() {
        setPadding(new Insets(1,1,1,1));
        setPrefWidth(93);
        setPrefColumns(3);
        setAlignment(Pos.CENTER_RIGHT);
        getStyleClass().add("widget-bar");
        initMouseEvents();
        setUpUI();
    }

    private void setUpUI() {
        btnPlus = new Button("+");
        btnPlus.setTooltip(new Tooltip("Increase Stroke"));
        btnPlus.setPrefWidth(30);
        btnPlus.setOnMousePressed(this::handlePlus);

        btnMinus = new Button("-");
        btnMinus.setTooltip(new Tooltip("Decrease Stroke"));
        btnMinus.setPrefWidth(30);
        btnMinus.setOnMousePressed(this::handleMinus);

        width = new Text("1");
        getChildren().add(btnMinus);
        getChildren().add(width);
        getChildren().add(btnPlus);
    }

    private void handlePlus(MouseEvent mouseEvent) {
        int width = Integer.parseInt(this.width.getText());
        width++;
        this.width.setText(String.valueOf(width));
    }
    private void handleMinus(MouseEvent mouseEvent) {
        int width = Integer.parseInt(this.width.getText());
        if (width != 1) {
            width--;
            this.width.setText(String.valueOf(width));
        }
    }

    public int getStrokeWidth() {
        return Integer.parseInt(this.width.getText());
    }

    /**
     * Initializes all mouse events for the widgetbar and updates associated class variables
     */
    private void initMouseEvents() {
        setOnMouseEntered(e -> setMouseOver(true));
        setOnMouseExited(e -> setMouseOver(false));
    }

    public boolean isMouseOver() {
        return isMouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        isMouseOver = mouseOver;
    }

    @Override
    public void update() {

    }
}

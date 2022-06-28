package com.quickshot.quickshot.ui;

import com.quickshot.quickshot.utilities.WidgetDrawData;
import com.quickshot.quickshot.ui.abstracts.WidgetBar;
import com.quickshot.quickshot.utilities.DisplayElement;
import com.quickshot.quickshot.utilities.Monitor;
import javafx.geometry.Orientation;

public class WidgetBarCommandTools extends WidgetBar implements DisplayElement {
    private final WidgetClose closeButton;
    private final Widget saveScreenshot;

    public WidgetBarCommandTools(ViewfinderBoundingBox boundingBox, WidgetDrawData drawData) {
        super(boundingBox, drawData);
        setOrientation(Orientation.VERTICAL);

        // create widgets
        saveScreenshot = new Widget("save.png");
        saveScreenshot.setClickableOnly();
        addWidget(saveScreenshot);
        closeButton = new WidgetClose("close.png");
        addWidget(closeButton);
    }

    public void calculateScreenPosition() {
        final int OFFSET = 10;
        final int HEIGHT_OF_DRAWING_TOOLS = 30;
        double Y_PADDING;
        double X_PADDING;

        if (getBoundingBox().getMiddleRight().getX() + getWidth() + OFFSET > Monitor.getMonitorWidth()) {
            X_PADDING = -getWidth() - 5;

            // checks if the bottom drawing tools bar is readjusted, if so this bar will sit above it instead of overlapping
            if (getBoundingBox().getBottomMiddle().getY() + HEIGHT_OF_DRAWING_TOOLS + OFFSET > Monitor.getMonitorHeight())
                Y_PADDING = -HEIGHT_OF_DRAWING_TOOLS - 10;
            else
                Y_PADDING = -5;
        } else {
            Y_PADDING = 0;
            X_PADDING = 5;
        }

        setTranslateX(getBoundingBox().getBottomRight().getX() + X_PADDING);
        setTranslateY(getBoundingBox().getBottomMiddle().getY() - getHeight() + Y_PADDING);
        setViewOrder(-1); // todo fix this
    }

    public WidgetClose getCloseButton() {
        return closeButton;
    }

    @Override
    public void update() {
        calculateScreenPosition();
    }

    public Widget getSaveButton() {
        return saveScreenshot;
    }
}

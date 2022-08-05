/**
 * WidgetBarCommandTools.java
 * @Description: The widget bar located on the right side of the viewfinder that houses all of the screenshot
 * commands like save, upload, copy. This is the graphical element of the bar along with the controls for
 * functionality
 */
package com.quickshot.quickshot.ui;

import com.quickshot.quickshot.utilities.ViewfinderViewOrder;
import com.quickshot.quickshot.utilities.WidgetDrawData;
import com.quickshot.quickshot.ui.abstracts.WidgetBar;
import com.quickshot.quickshot.utilities.DisplayElement;
import com.quickshot.quickshot.utilities.Monitor;

public class WidgetBarCommandTools extends WidgetBar implements DisplayElement {
    private final Widget closeButton;
    private final Widget saveScreenshot;
    private final Widget copyScreenshot;
    private final Widget uploadScreenshot;
    private final Widget readText;

    public WidgetBarCommandTools(ViewfinderBoundingBox boundingBox, WidgetDrawData drawData) {
        super(boundingBox, drawData);
        setPrefColumns(1);
        setVgap(3);

        // creates all widgets on the widget bar and adds them to it
        readText = new Widget("search", "Translate Text");
        readText.setClickableOnly();
        addWidget(readText);
        uploadScreenshot = new Widget("upload", "Upload for URL");
        uploadScreenshot.setClickableOnly();
        addWidget(uploadScreenshot);
        saveScreenshot = new Widget("save", "Save Screen Capture");
        saveScreenshot.setClickableOnly();
        addWidget(saveScreenshot);
        copyScreenshot = new Widget("copy", "Copy Screen Capture");
        copyScreenshot.setClickableOnly();
        addWidget(copyScreenshot);
        closeButton = new Widget("close", "Minimise Program");
        addWidget(closeButton);
    }

    /**
     * Calculates the screen position of the widget bar
     */
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
        setViewOrder(ViewfinderViewOrder.WIDGET_BAR);
    }

    public Widget getCloseButton() {
        return closeButton;
    }

    public Widget getSaveButton() {
        return saveScreenshot;
    }

//    public Widget getRecordGif() {
//        return recordGif;
//    }

    public Widget getCopyScreenshot() {
        return copyScreenshot;
    }

    public Widget getUploadScreenshot() {
        return uploadScreenshot;
    }

    public Widget getReadText() {
        return readText;
    }

    @Override
    public void update() {
        calculateScreenPosition();
    }
}

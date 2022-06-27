package com.quickshot.quickshot;


import java.util.ArrayList;
import java.util.List;

public class ViewfinderControls {
    private final WidgetBarDrawingTools drawingToolBar;
    private final WidgetDrawData drawData;

    public ViewfinderControls(ViewfinderBoundingBox boundingBox) {
        drawData = new WidgetDrawData();
        drawingToolBar = new WidgetBarDrawingTools(boundingBox, drawData);
    }

    public ArrayList<DisplayElement> getAllWidgetBars() {
        return new ArrayList<>(List.of(
                getDrawingToolBar()
        ));
    }

    public void update() {
        getDrawingToolBar().update();
    }
    public WidgetDrawData getDrawData() {
        return drawData;
    }

    public WidgetBarDrawingTools getDrawingToolBar() {
        return drawingToolBar;
    }

    public boolean isMouseOver() {
        if (getDrawingToolBar().isMouseOver()) {
            return true;
        }
        return false;
    }
}

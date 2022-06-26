package com.quickshot.quickshot;

public class WidgetUndo extends Widget {
    private WidgetDrawData drawData;

    public WidgetUndo(String fileName, WidgetDrawData drawData) {
        super(fileName);
        this.drawData = drawData;
    }

    @Override
    public void defaultMouseEvents() {

    }

    public void handleButtonPress() {
        setFocused(false);
    }
}


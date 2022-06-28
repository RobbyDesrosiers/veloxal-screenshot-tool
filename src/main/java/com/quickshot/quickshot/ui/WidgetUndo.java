package com.quickshot.quickshot.ui;

import com.quickshot.quickshot.ui.Widget;
import com.quickshot.quickshot.utilities.WidgetDrawData;

public class WidgetUndo extends Widget {
    private WidgetDrawData drawData;

    public WidgetUndo(String fileName, WidgetDrawData drawData) {
        super(fileName);
        this.drawData = drawData;
    }

    @Override
    public void defaultMouseEvents() {

    }
}


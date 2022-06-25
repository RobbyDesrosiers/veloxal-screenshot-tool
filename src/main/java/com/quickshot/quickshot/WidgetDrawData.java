package com.quickshot.quickshot;

import javafx.scene.Node;
import java.util.LinkedList;

public class WidgetDrawData extends LinkedList<Node> {
    private boolean newLine;

    public WidgetDrawData() {
        newLine = false;
    }
    public void setNewLine(boolean b) {
        newLine = b;
    }
    public boolean isNewLine() {
        return newLine;
    }
}

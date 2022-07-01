package com.quickshot.quickshot.utilities;

import javafx.scene.Node;
import java.util.LinkedList;

public class WidgetDrawData extends LinkedList<LinkedList<Node>> {
    private boolean newLine;

    // holds the currently-being-drawn objects until widget is no longer 'drawing' (checked with isDrawing Function)
    // tempData will be offloaded into WidgetDrawData LinkedList<LinkedList<Node>> on mouse release in refreshScreen()
    private final LinkedList<Node> tempData;

    // deleted data gets added when user pressed 'undoButton' in ViewfinderWidgetBar class
    // deleted data gets removed from screen in refreshScreen() of UserController class
    private final LinkedList<LinkedList<Node>> deletedData;

    public WidgetDrawData() {
        newLine = false;
        tempData = new LinkedList<>();
        deletedData = new LinkedList<>();
    }

    public void setNewLine(boolean b) {
        newLine = b;
    }

    public boolean isNewLine() {
        return newLine;
    }

    public void setVisible(boolean b) {
        for (LinkedList<Node> dataList : this) {
            for (Node node : dataList) {
                node.setVisible(b);
            }
        }
    }

    public LinkedList<LinkedList<Node>> getDeletedData() {
        return deletedData;
    }

    public void deleteAllData() {
        for (LinkedList<Node> data : this) {
            deletedData.push(data);
        }
    }

    public LinkedList<Node> getTempData() {
        return tempData;
    }

    public void saveTempDataToDrawData() {
        push(new LinkedList<>(tempData));
        tempData.clear();
    }
}

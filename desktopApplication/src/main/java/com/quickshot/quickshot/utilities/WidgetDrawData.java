/**
 * WidgetDrawData.java
 * @Description: Holds all drawn objects in the LinkedList. Objects drawn onto screen are stored as a LinkedList<Node>
 * which is offloaded into the WidgetDrawData extended LinkedList once the user un-presses their mouse
 * The drawn objects are stored as LinkedList<Nodes> in order to easily allow the 'Undo Draw Button' to remove
 * drawn-sections at a time instead of removing random/arbitrary sections.
 */

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

    /**
     * When mouse is picked up (unpressed mouse button) this function will fire
     * @param b
     */
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

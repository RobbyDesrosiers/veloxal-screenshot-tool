package com.quickshot.quickshot.utilities;

import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProgramTray {
    SystemTray tray = SystemTray.getSystemTray();
    String PROGRAM_TITLE = "QuickShot";
    double VERSION = 0.2;
    Image trayImg = Toolkit.getDefaultToolkit().createImage("src/main/java/com/quickshot/quickshot/resources/logo.png");
    TrayIcon trayIcon = new TrayIcon(trayImg, PROGRAM_TITLE + " " + VERSION);

    MenuItem exitMenuButton;

    public ProgramTray() throws AWTException {
        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip(PROGRAM_TITLE + " " + VERSION);

        PopupMenu popupMenu = new PopupMenu();
        exitMenuButton = new MenuItem("Exit");
        popupMenu.add(exitMenuButton);
        trayIcon.setPopupMenu(popupMenu);
        tray.add(trayIcon);
    }

    public void displayMessage(String text, MessageType messageType) {
        getTrayIcon().displayMessage(PROGRAM_TITLE, text, messageType);
    }

    public TrayIcon getTrayIcon() {
        return trayIcon;
    }

    public MenuItem getExitMenuButton() {
        return exitMenuButton;
    }
}

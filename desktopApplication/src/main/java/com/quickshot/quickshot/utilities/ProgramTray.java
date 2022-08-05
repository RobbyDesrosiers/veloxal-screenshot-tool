package com.quickshot.quickshot.utilities;

import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProgramTray {
    SystemTray tray = SystemTray.getSystemTray();
    Image trayImg = Toolkit.getDefaultToolkit().createImage(ClassLoader.getSystemResource("copy_light.png"));
    TrayIcon trayIcon = new TrayIcon(trayImg, ProgramInfo.TITLE);

    MenuItem exitMenuButton;

    public ProgramTray() throws AWTException {
        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip(ProgramInfo.TITLE);

        PopupMenu popupMenu = new PopupMenu();
        exitMenuButton = new MenuItem("Exit");
        popupMenu.add(exitMenuButton);
        trayIcon.setPopupMenu(popupMenu);
        tray.add(trayIcon);
    }

    public void displayMessage(String text, MessageType messageType) {
        getTrayIcon().displayMessage(ProgramInfo.TITLE, text, messageType);
    }

    public TrayIcon getTrayIcon() {
        return trayIcon;
    }

    public MenuItem getExitMenuButton() {
        return exitMenuButton;
    }
}

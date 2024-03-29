package com.desrosiersrobby.veloxal.utilities;

import java.awt.*;
import java.awt.TrayIcon.MessageType;

public class ProgramTray {
    SystemTray tray = SystemTray.getSystemTray();
    Image trayImg = Toolkit.getDefaultToolkit().createImage(ClassLoader.getSystemResource("veloxal_logo_1x1.png"));
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

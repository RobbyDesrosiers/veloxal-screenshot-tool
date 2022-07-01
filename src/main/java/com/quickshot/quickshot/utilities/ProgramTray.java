package com.quickshot.quickshot.utilities;

import java.awt.*;
import java.awt.TrayIcon.MessageType;

public class ProgramTray {
    SystemTray tray = SystemTray.getSystemTray();
    String PROGRAM_TITLE = "QuickShot";
    double VERSION = 0.2;
    Image trayImg = Toolkit.getDefaultToolkit().createImage("src/main/java/com/quickshot/quickshot/resources/logo.png");
    TrayIcon trayIcon = new TrayIcon(trayImg, PROGRAM_TITLE + " " + VERSION);
    //        trayIcon.displayMessage("Hello, World", "notification demo", MessageType.INFO);

    public ProgramTray() throws AWTException {
        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip(PROGRAM_TITLE + " " + VERSION);
        tray.add(trayIcon);
    }

    public TrayIcon getTrayIcon() {
        return trayIcon;
    }
}

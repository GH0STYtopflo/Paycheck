package ai.ghosty.paycheck.authentication;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class QuickLogin {
    public static String generateCode() {
        Random random = new Random();
        String code = String.valueOf(random.nextInt(0, 10000));
        if (!pushNotification(code)) code = "-1";

        return code;
    }

    private static boolean pushNotification(String code) {
        if (!SystemTray.isSupported()) {
           return false;
        }

        SystemTray tray = SystemTray.getSystemTray();
        Image image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
        TrayIcon trayIcon = new TrayIcon(image);
        trayIcon.setImageAutoSize(true);
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println(e.getMessage());
            return false;
        }
        trayIcon.displayMessage("Verification Code", code, TrayIcon.MessageType.INFO);

        return true;
    }
}

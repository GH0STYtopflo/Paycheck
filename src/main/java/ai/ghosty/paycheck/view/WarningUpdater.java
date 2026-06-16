package ai.ghosty.paycheck.view;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class WarningUpdater {
    /** op code: 0 = success 1 = fail
     * @param op
     * @param message
     * @param lblWarning
     */
    public static void updateWarningText(byte op, String message, Label lblWarning) {
        lblWarning.setText(message);
        lblWarning.setTextFill(op == 1 ? Color.RED : Color.GREEN);
        lblWarning.setVisible(true);
    }
}

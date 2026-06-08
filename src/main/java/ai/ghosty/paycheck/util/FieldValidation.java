package ai.ghosty.paycheck.util;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.math.BigDecimal;

public class FieldValidation {
    // op codes: 1 = check empty 2 = check numeric 3 = check both
    public static boolean validateFields(TextField[] fields, byte op, Label lblWarning) {
        for (TextField f : fields) {
            if (op == 1 || op == 3) {
                if (f.getText().isEmpty()) {
                    updateWarningText((byte) 1, "Please fill out all fields", lblWarning);
                    return false;
                }
            }

            if (op == 2 || op == 3) {
                try {
                    new BigDecimal(f.getText());
                }
                catch (NumberFormatException e) {
                    updateWarningText((byte) 1, "Please fill out fields correctly", lblWarning);
                    return false;
                }
            }
        }

        return true;
    }

    // op code: 0 = success 1 = fail
    public static void updateWarningText(byte op, String message, Label lblWarning) {
        lblWarning.setText(message);
        lblWarning.setTextFill(op == 1 ? Color.RED : Color.GREEN);
        lblWarning.setVisible(true);
    }
}

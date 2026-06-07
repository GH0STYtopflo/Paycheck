package ai.ghosty.paycheck.util;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class FieldValidation {
    // op codes: 1 = check empty 2 = check numeric 3 = check both
    public static boolean validateFields(TextField[] fields, byte op, Label lblWarning) {
        for (TextField f : fields) {
            if (op == 1 || op == 3) {
                if (f.getText().isEmpty()) {
                    lblWarning.setText("Please fill out all fields");
                    lblWarning.setTextFill(Color.RED);
                    lblWarning.setVisible(true);
                    return false;
                }
            }

            if (op == 2 || op == 3) {
                try {
                    Integer.parseInt(f.getText());
                }
                catch (NumberFormatException e) {
                    lblWarning.setText("Please fill out fields correctly");
                    lblWarning.setTextFill(Color.RED);
                    lblWarning.setVisible(true);
                    return false;
                }
            }
        }

        return true;
    }
}

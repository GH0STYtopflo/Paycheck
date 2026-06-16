package ai.ghosty.paycheck.validation;

import javafx.scene.control.TextField;

import java.math.BigDecimal;

public class Validator {

    //عین علامت ستاره در پایتون هست که به ما اجازه میدهد به تعداد غیر مشخص دستور بگیریم
    // I hate python but yeah sure why not
    public static ValidationResult validate(TextField field, Rule... rules) {
        //یک ابجکت برای ذخیره داده میسازیم
        ValidationResult result = new ValidationResult();

        // I edited this method so we can send a field instead of a String
        String value = field.getText();

        for (Rule rule : rules) {

            switch (rule) {
                case NOT_BLANK:
                    // //برسی میکند مقدار خالی نباشد 
                    if (value == null || value.trim().isEmpty()) {
                        result.addError("Values must not be blank");
                    }
                    break;
                //این متد بررسی می‌کند آیا مقدار عدد است یا نه  
                case NUMERIC:
                    if (!isNumeric(value)) {
                        result.addError("Values must be numeric");
                    }
                    break;

                case NON_NEGATIVE:
                    if (!isPositive(value)) {
                        result.addError("Values must be positive");
                    }
                    break;
            }
        }

        return result;
    }


    // for validating multiple fields
    public static ValidationResult validate(TextField[] fields, Rule... rules) {
        //یک ابجکت برای ذخیره داده میسازیم
        ValidationResult result = new ValidationResult();

        for (TextField field : fields) {
            String value = field.getText();
            boolean valid = true;

            for (Rule rule : rules) {


                switch (rule) {
                    case NOT_BLANK:
                        // //برسی میکند مقدار خالی نباشد
                        if (value == null || value.trim().isEmpty()) {
                            result.addError("Values must not be blank");
                            valid = false;
                        }
                        break;
                    //این متد بررسی می‌کند آیا مقدار عدد است یا نه
                    case NUMERIC:
                        if (!isNumeric(value)) {
                            result.addError("Values must be numeric");
                            valid = false;
                        }
                        break;

                    case NON_NEGATIVE:
                        if (!isPositive(value)) {
                            result.addError("Values must be non negative");
                            valid = false;
                        }
                        break;
                }
            }

            // don't check other fields if one is invalid
            if (!valid) break;
        }

        return result;
    }


    private static boolean isNumeric(String value) {
        if (value == null || value.isEmpty()) return false;

        try {
            //اگر تبدیل به عدد موفق هست
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //عدد باید بزرگتر= از صفر باشد و عدد هم باشد
    private static boolean isPositive(String value) {
        try {
            return new BigDecimal(value).compareTo(BigDecimal.ZERO) >= 0;
        } catch (Exception e) {
            return false;
        }
    }
}

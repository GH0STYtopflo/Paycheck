package ai.ghosty.paycheck.validation;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {

    private boolean valid = true;
    private List<String> errors = new ArrayList<>();

    public void addError(String error) {
        valid = false;
        errors.add(error);
    }

    public boolean isValid() {
        return valid;
    }
    
    public List<String> getErrors() {
        return errors;
    }

    // add this method so we can get the first invalid input error
    public String getError(){return errors.getFirst();}
}

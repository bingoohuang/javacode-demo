package org.n3r.sandbox.validation.contraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<Email, String> {

    private final static Pattern EMAIL_PATTERN = Pattern.compile(".+@.+\\.[a-z]+");

    public void initialize(Email constraintAnnotation) {
        // nothing to initialize
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        return EMAIL_PATTERN.matcher(value).matches();
    }
}

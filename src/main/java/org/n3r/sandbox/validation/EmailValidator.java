package org.n3r.sandbox.validation;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

public class EmailValidator implements Validator {

    private final static Pattern EMAIL_PATTERN = Pattern.compile(".+@.+\\.[a-z]+");

    public boolean supports(Class type) {
        return Email.class.equals(type);
    }

    public void validate(Object target, Errors errors) {
        Email email = (Email) target;

        String from = email.getFrom();
        if (!StringUtils.hasText(from)) {
            errors.rejectValue("from", "required");
        } else if (!isEmail(from)) {
            errors.rejectValue("from", "invalid.email");
        }

        String to = email.getTo();
        if (!StringUtils.hasText(to)) {
            errors.rejectValue("from", "required");
        } else if (!isEmail(to)) {
            errors.rejectValue("from", "invalid.email");
        }

        ValidationUtils.rejectIfEmpty(errors, "subject", "required");
        ValidationUtils.rejectIfEmpty(errors, "body", "required");
    }

    private boolean isEmail(String value) {
        return EMAIL_PATTERN.matcher(value).matches();
    }

}

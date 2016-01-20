package org.n3r.sandbox.validation;

import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

public class SpringMVCValidationDemo {
    public static void main(String[] args) {
        Email email = new Email();
        email.setFrom("john@domain.com");
        email.setTo("someone");
        email.setSubject("");
        email.setBody(null);

        BindException errors = new BindException(email, "email");
        EmailValidator validator = new EmailValidator();
        validator.validate(email, errors);

        for (FieldError error : errors.getFieldErrors()) {
            System.out.println("invalid value for: '" + error.getField() + "': " + error.getDefaultMessage());
        }
    }
}

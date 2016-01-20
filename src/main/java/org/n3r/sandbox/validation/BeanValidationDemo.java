package org.n3r.sandbox.validation;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class BeanValidationDemo {
    public static void main(String[] args) {
        Email email = new Email();
        email.setFrom("john@domain.com");
        email.setTo("someone");
        email.setSubject("");
        email.setBody(null);

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        Set<ConstraintViolation<Email>> violations = validator.validate(email);
        for (ConstraintViolation<Email> constraintViolation : violations) {
            String propertyPath = constraintViolation.getPropertyPath().toString();
            String message = constraintViolation.getMessage();
            System.out.println("invalid value for: '" + propertyPath + "': " + message);
        }

    }
}

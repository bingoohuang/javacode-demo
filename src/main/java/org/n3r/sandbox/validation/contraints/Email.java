package org.n3r.sandbox.validation.contraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Email {

    public abstract String message() default "{package.name.Email.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}


package com.github.mangila.webshop.common.util.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AlphaNumericValidator.class)
@Documented
public @interface AlphaNumeric {
    String message() default "Value must be alphanumeric (letters and digits)";

    boolean allowNull() default true;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

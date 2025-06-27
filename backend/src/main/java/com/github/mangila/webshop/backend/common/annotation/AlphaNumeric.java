package com.github.mangila.webshop.backend.common.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AlphaNumericValidator.class)
@Documented
public @interface AlphaNumeric {
    String message() default "Value must be alphanumeric (letters and digits)";

    boolean allowNull() default true;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

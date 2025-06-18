package com.github.mangila.webshop.common.util.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = JsonValidator.class)
@Documented
public @interface Json {
    String message() default "Value must be alphanumeric (letters and digits)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

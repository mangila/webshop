package com.github.mangila.webshop.identity.application.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {
        GeneratedIdentityValidator.class
})
@Documented
public @interface GeneratedIdentity {

    String message() default "Not a valid ID";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

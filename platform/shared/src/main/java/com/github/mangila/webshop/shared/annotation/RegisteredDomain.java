package com.github.mangila.webshop.shared.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {
        RegisteredDomainValidator.class,
        RegisteredDomainStringValidator.class
})
@Documented
public @interface RegisteredDomain {
    String message() default "Domain is not Registered";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

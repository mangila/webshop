package com.github.mangila.webshop.shared.infrastructure.spring.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.*;

@NotNull
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {DomainIdValidator.class})
@Documented
public @interface DomainId {

    String message() default "Invalid domain ID";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

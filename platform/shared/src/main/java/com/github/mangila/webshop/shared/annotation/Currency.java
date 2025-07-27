package com.github.mangila.webshop.shared.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CurrencyValidator.class)
@Documented
@NotBlank
@Size(min = 3, max = 3)
public @interface Currency {
    String message() default "Unknown Currency";

    String[] currencies() default {};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

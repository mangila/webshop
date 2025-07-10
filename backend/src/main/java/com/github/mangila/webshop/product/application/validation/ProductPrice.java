package com.github.mangila.webshop.product.application.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.lang.annotation.*;

@NotNull
@Positive(message = "Product price must be positive")
@Digits(integer = 10, fraction = 2, message = "Product price must have at most 10 integer digits and 2 decimal places")
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface ProductPrice {
    String message() default "Not a valid Product price";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

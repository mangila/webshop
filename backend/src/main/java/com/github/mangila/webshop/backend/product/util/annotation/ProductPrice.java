package com.github.mangila.webshop.backend.product.util.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {}) // No custom validator needed
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@NotNull(message = "Price is required")
@DecimalMin(value = "0.01", message = "Price must be at least 0.01")
@DecimalMax(value = "99999.99", message = "Price must not exceed 99,999.99")
@Digits(integer = 7, fraction = 2, message = "Price format is invalid (up to 7 digits and 2 decimal places)")
public @interface ProductPrice {
    String message() default "Invalid product Price";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

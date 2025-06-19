package com.github.mangila.webshop.backend.product.util.annotation;

import com.github.mangila.webshop.backend.common.util.annotation.AlphaNumeric;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {}) // No custom validator needed
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@NotNull(message = "name must not be null")
@AlphaNumeric(allowNull = false)
@Size(min = 1, max = 255)
public @interface ProductName {
    String message() default "Invalid product Name";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

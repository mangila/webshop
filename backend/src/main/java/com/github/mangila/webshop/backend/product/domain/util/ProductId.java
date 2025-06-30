package com.github.mangila.webshop.backend.product.domain.util;

import com.github.mangila.webshop.backend.common.annotation.AlphaNumeric;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {}) // No custom validator needed
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@NotNull(message = "id must not be null")
@AlphaNumeric(allowNull = false)
@Size(min = 1, max = 36)
public @interface ProductId {
    String message() default "Invalid product ID";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

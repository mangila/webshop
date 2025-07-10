package com.github.mangila.webshop.product.application.validation;

import com.github.mangila.webshop.shared.infrastructure.spring.validation.AlphaNumeric;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@NotBlank
@Size(min = 2, max = 100)
@AlphaNumeric(
        withHyphen = true,
        withSpace = true,
        message = "Product name must only contain alphanumeric characters")
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface ProductName {
    String message() default "Not a valid Product name";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
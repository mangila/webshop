package com.github.mangila.webshop.product.application.validation;

import com.github.mangila.webshop.shared.infrastructure.spring.validation.Currency;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@NotBlank
@Size(min = 3, max = 3)
@Currency(message = "Must be a valid Currency Code")
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface ProductCurrency {
    String message() default "Not a valid Product Currency Code";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

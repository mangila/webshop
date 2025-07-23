package com.github.mangila.webshop.price;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.lang.annotation.*;

@NotNull
@Positive(message = "Amount must be positive")
@Digits(integer = 10, fraction = 2, message = "Amount must have at most 10 integer digits and 2 decimal places")
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface Amount {
    String message() default "Not a valid amount";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

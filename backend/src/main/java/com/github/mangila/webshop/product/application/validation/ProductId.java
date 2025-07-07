package com.github.mangila.webshop.product.application.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.*;

@NotNull
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ProductIdValidator.class})
@Documented
public @interface ProductId {

    String message() default "Invalid product ID";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

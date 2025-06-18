package com.github.mangila.webshop.common.util.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.net.URI;

public class UriValidator implements ConstraintValidator<Uri, String> {

    private String protocol;
    private boolean allowNull;

    @Override
    public void initialize(Uri constraintAnnotation) {
        this.protocol = constraintAnnotation.protocol();
        this.allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return allowNull;
        }
        if (!value.startsWith(protocol)) {
            return false;
        }
        return URI.create(value).isAbsolute();
    }
}

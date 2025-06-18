package com.github.mangila.webshop.common.util.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.net.URI;

public class UriValidator implements ConstraintValidator<Uri, String> {

    private String protocol;

    @Override
    public void initialize(Uri constraintAnnotation) {
        this.protocol = constraintAnnotation.protocol();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        if (!value.startsWith(protocol)) {
            return false;
        }
        return URI.create(value).isAbsolute();
    }
}

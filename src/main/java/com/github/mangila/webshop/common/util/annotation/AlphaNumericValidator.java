package com.github.mangila.webshop.common.util.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class AlphaNumericValidator implements ConstraintValidator<AlphaNumeric, String> {

    private static final Pattern ALPHA_NUMERIC = Pattern.compile("^[a-zA-Z0-9\\s]+$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return ALPHA_NUMERIC.matcher(value).matches();
    }
}

package com.github.mangila.webshop.shared.application.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class AlphaNumericValidator implements ConstraintValidator<AlphaNumeric, String> {

    private static final Pattern ALPHA_NUMERIC = Pattern.compile("^[a-zA-Z0-9\\s-]+$");

    private boolean allowNull;

    @Override
    public void initialize(AlphaNumeric constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return allowNull;
        }
        return ALPHA_NUMERIC.matcher(value).matches();
    }
}

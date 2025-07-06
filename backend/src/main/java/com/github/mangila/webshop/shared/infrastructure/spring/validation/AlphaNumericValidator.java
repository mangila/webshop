package com.github.mangila.webshop.shared.infrastructure.spring.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class AlphaNumericValidator implements ConstraintValidator<AlphaNumeric, String> {

    private static final Pattern ALPHA_NUMERIC = Pattern.compile("^[a-zA-Z0-9]+$");
    private static final Pattern ALPHA_NUMERIC_SPACE = Pattern.compile("^[a-zA-Z0-9\\s]+$");
    private static final Pattern ALPHA_NUMERIC_HYPHEN = Pattern.compile("^[a-zA-Z0-9-]+$");
    private static final Pattern ALPHA_NUMERIC_SPACE_HYPHEN = Pattern.compile("^[a-zA-Z0-9\\s-]+$");

    private boolean allowNull;
    private Pattern pattern;

    @Override
    public void initialize(AlphaNumeric constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
        boolean withHyphen = constraintAnnotation.withHyphen();
        boolean withSpace = constraintAnnotation.withSpace();
        if (withHyphen && withSpace) {
            this.pattern = ALPHA_NUMERIC_SPACE_HYPHEN;
        } else if (withHyphen) {
            this.pattern = ALPHA_NUMERIC_HYPHEN;
        } else if (withSpace) {
            this.pattern = ALPHA_NUMERIC_SPACE;
        } else {
            this.pattern = ALPHA_NUMERIC;
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return allowNull;
        }
        return pattern.matcher(value).matches();
    }
}

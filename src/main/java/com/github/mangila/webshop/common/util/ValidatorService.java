package com.github.mangila.webshop.common.util;

import jakarta.validation.Validator;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ValidatorService {

    private final Validator validator;

    public ValidatorService(Validator validator) {
        this.validator = validator;
    }

    public <T> Set<String> validateFields(T object, String... fieldNames) {
        Set<String> errors = new HashSet<>();
        for (String fieldName : fieldNames) {
            errors.addAll(validateField(object, fieldName));
        }
        return errors;
    }

    public <T> Set<String> validateField(T object, String fieldName) {
        return validator.validateProperty(object, fieldName)
                .stream()
                .map(err -> String.format("%s: %s -- %s", fieldName,
                        err.getMessage(),
                        err.getInvalidValue()))
                .collect(Collectors.toSet());
    }

    public <T> void ensureValidateField(T object, String fieldName) {
        Set<String> errors = validator.validateProperty(object, fieldName)
                .stream()
                .map(err -> String.format("%s: %s -- %s", fieldName,
                        err.getMessage(),
                        err.getInvalidValue()))
                .collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(errors)) {
            throw new ValidationException(String.join(", ", errors));
        }
    }
}

package com.github.mangila.webshop.backend.common.annotation;

import com.github.mangila.webshop.backend.common.util.JsonMapper;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

@Component
public class JsonValidator implements ConstraintValidator<Json, String> {

    private final JsonMapper jsonMapper;
    private boolean allowNull;

    public JsonValidator(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    @Override
    public void initialize(Json constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(@Nullable String value, ConstraintValidatorContext context) {
        if (value == null) {
            return allowNull;
        }
        return jsonMapper.isValid(value);
    }
}

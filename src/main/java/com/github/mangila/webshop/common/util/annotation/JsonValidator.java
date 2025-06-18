package com.github.mangila.webshop.common.util.annotation;

import com.github.mangila.webshop.common.util.JsonMapper;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class JsonValidator implements ConstraintValidator<Json, String> {

    private final JsonMapper jsonMapper;

    public JsonValidator(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return jsonMapper.isValid(value);
    }
}

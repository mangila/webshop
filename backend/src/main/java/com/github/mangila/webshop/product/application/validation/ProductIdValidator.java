package com.github.mangila.webshop.product.application.validation;

import com.github.mangila.webshop.shared.uuid.application.UuidGeneratorService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductIdValidator implements ConstraintValidator<ProductId, UUID> {

    private final UuidGeneratorService uuidGeneratorService;

    public ProductIdValidator(UuidGeneratorService uuidGeneratorService) {
        this.uuidGeneratorService = uuidGeneratorService;
    }

    @Override
    public boolean isValid(UUID value, ConstraintValidatorContext context) {
        return uuidGeneratorService.hasGenerated(value);
    }
}

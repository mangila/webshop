package com.github.mangila.webshop.product;

import com.github.mangila.webshop.common.util.ValidationException;
import com.github.mangila.webshop.common.util.ValidatorService;
import com.github.mangila.webshop.product.model.ProductCommand;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProductValidator {

    private static final Logger log = LoggerFactory.getLogger(ProductValidator.class);

    private final ValidatorService validatorService;

    public ProductValidator(ValidatorService validatorService) {
        this.validatorService = validatorService;
    }

    public void validate(ProductCommand command) {
        log.debug("Validating command -- {}", command);
        if (command == null || command.isEmpty()) {
            throw new IllegalArgumentException("command must not be null or empty");
        }
        validatorService.ensureValidateField(command, "type");
        Set<String> errors = switch (command.type()) {
            case UPSERT_PRODUCT -> validateUpsertProduct(command);
            case DELETE_PRODUCT -> validateDeleteProduct(command);
            case UPDATE_PRODUCT_PRICE -> validateUpdateProductPrice(command);
            case null -> throw new NullPointerException("command must not be null");
            default -> throw new IllegalArgumentException("command not supported:" + command);
        };
        if (!CollectionUtils.isEmpty(errors)) {
            throw new ValidationException(String.format("%s: %s", command, errors));
        }
    }

    private Set<String> validateUpsertProduct(ProductCommand command) {
        return validatorService.validateFields(command,
                "id",
                "name",
                "price",
                "attributes");
    }

    private Set<String> validateDeleteProduct(ProductCommand command) {
        return validatorService.validateField(command, "id");
    }

    private Set<String> validateUpdateProductPrice(ProductCommand command) {
        return validatorService.validateFields(command,
                "id",
                "price");
    }
}

package com.github.mangila.webshop.product;

import com.github.mangila.webshop.common.util.ValidationException;
import com.github.mangila.webshop.common.util.ValidatorService;
import com.github.mangila.webshop.product.model.ProductCommand;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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

    public void validate(@NotNull ProductCommand command) {
        log.info("Validating command -- {}", command);
        validatorService.validateField(command, "type");
        Set<String> errors = switch (command.type()) {
            case UPSERT_PRODUCT -> {
                var l = List.of(
                        validatorService.validateField(command, "id"),
                        validatorService.validateField(command, "name"),
                        validatorService.validateField(command, "description"),
                        validatorService.validateField(command, "price"),
                        validatorService.validateField(command, "imageUrl"),
                        validatorService.validateField(command, "category"),
                        validatorService.validateField(command, "extensions")
                );
                yield l.stream().flatMap(Set::stream).collect(Collectors.toSet());
            }
            case DELETE_PRODUCT -> {
                var l = List.of(
                        validatorService.validateField(command, "id")
                );
                yield l.stream().flatMap(Set::stream).collect(Collectors.toSet());
            }
            case UPDATE_PRODUCT_PRICE -> {
                var l = List.of(
                        validatorService.validateField(command, "id"),
                        validatorService.validateField(command, "price")
                );
                yield l.stream().flatMap(Set::stream).collect(Collectors.toSet());
            }
            case null -> throw new NullPointerException("command must not be null");
            default -> throw new IllegalArgumentException("command not supported:" + command);
        };
        if (!errors.isEmpty()) {
            throw new ValidationException(String.format("%s: %s", command, errors));
        }
    }
}


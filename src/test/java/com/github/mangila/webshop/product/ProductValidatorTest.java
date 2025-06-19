package com.github.mangila.webshop.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.webshop.common.util.JsonMapper;
import com.github.mangila.webshop.common.util.ValidationException;
import com.github.mangila.webshop.common.util.ValidatorService;
import com.github.mangila.webshop.common.util.annotation.JsonValidator;
import com.github.mangila.webshop.product.model.ProductCommand;
import com.github.mangila.webshop.product.model.ProductCommandType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(classes = {
        ProductValidator.class,
        ValidatorService.class,
        LocalValidatorFactoryBean.class,
        JsonMapper.class,
        JsonValidator.class,
        ObjectMapper.class})
class ProductValidatorTest {

    @Autowired
    private ProductValidator validator;

    @Test
    @DisplayName("Should throw IllegalArgumentException when ProductCommand is null")
    void shouldThrowExceptionWhenProductCommandIsNull() {
        assertThatThrownBy(() -> validator.validate(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("command must not be null or empty");
    }

    @Test
    @DisplayName("Should throw ValidationException when ProductCommand type is null")
    void shouldThrowNullPointerExceptionWhenCommandTypeIsNull() {
        ProductCommand command = new ProductCommand(
                null, "id123", "Test Product", new BigDecimal("99.99"), "{}"
        );

        assertThatThrownBy(() -> validator.validate(command))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Command type must not be null -- null");
    }

    // This test is removed because we can't easily test the default case with the current enum values
    // All enum values are handled in the switch statement

    @Test
    @DisplayName("Should throw ValidationException when UPSERT_PRODUCT command has invalid fields")
    void shouldThrowValidationExceptionWhenUpsertProductCommandHasInvalidFields() {
        // Missing name and price
        ProductCommand command = new ProductCommand(
                ProductCommandType.UPSERT_PRODUCT, "id123", null, null, "{}"
        );

        assertThatThrownBy(() -> validator.validate(command))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("name must not be null")
                .hasMessageContaining("Price is required");
    }

    @Test
    @DisplayName("Should throw ValidationException when DELETE_PRODUCT command has invalid id")
    void shouldThrowValidationExceptionWhenDeleteProductCommandHasInvalidId() {
        // Empty id
        ProductCommand command = new ProductCommand(
                ProductCommandType.DELETE_PRODUCT, "", "Test Product", new BigDecimal("99.99"), "{}"
        );

        assertThatThrownBy(() -> validator.validate(command))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("id");
    }

    @Test
    @DisplayName("Should throw ValidationException when UPDATE_PRODUCT_PRICE command has invalid price")
    void shouldThrowValidationExceptionWhenUpdateProductPriceCommandHasInvalidPrice() {
        // Negative price
        ProductCommand command = new ProductCommand(
                ProductCommandType.UPDATE_PRODUCT_PRICE, "id123", "Test Product", new BigDecimal("-1.00"), "{}"
        );

        assertThatThrownBy(() -> validator.validate(command))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Price must be at least 0.01");
    }

    @Test
    @DisplayName("Should not throw exception for valid UPSERT_PRODUCT command")
    void shouldNotThrowExceptionForValidUpsertProductCommand() {
        ProductCommand command = new ProductCommand(
                ProductCommandType.UPSERT_PRODUCT, "id123", "Test Product", new BigDecimal("99.99"), "{}"
        );

        assertDoesNotThrow(() -> validator.validate(command));
    }

    @Test
    @DisplayName("Should not throw exception for valid DELETE_PRODUCT command")
    void shouldNotThrowExceptionForValidDeleteProductCommand() {
        ProductCommand command = new ProductCommand(
                ProductCommandType.DELETE_PRODUCT, "id123", "Test Product", new BigDecimal("99.99"), "{}"
        );

        assertDoesNotThrow(() -> validator.validate(command));
    }

    @Test
    @DisplayName("Should not throw exception for valid UPDATE_PRODUCT_PRICE command")
    void shouldNotThrowExceptionForValidUpdateProductPriceCommand() {
        ProductCommand command = new ProductCommand(
                ProductCommandType.UPDATE_PRODUCT_PRICE, "id123", "Test Product", new BigDecimal("99.99"), "{}"
        );

        assertDoesNotThrow(() -> validator.validate(command));
    }
}

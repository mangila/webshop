package com.github.mangila.webshop.product;

import com.github.mangila.webshop.product.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = {ProductValidator.class})
class ProductValidatorTest {

    @Autowired
    private ProductValidator validator;

    @Test
    void shouldThrowExceptionWhenProductIsNull() {
        assertThatThrownBy(() -> validator.validateByCommand(null, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("product must not be null");
    }

    @Test
    void shouldThrowExceptionWhenCommandIsNull() {
        assertThatThrownBy(() -> validator.validateByCommand(null, new Product()))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("command must not be null");
    }
}
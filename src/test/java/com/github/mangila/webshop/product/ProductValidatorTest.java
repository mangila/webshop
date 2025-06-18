package com.github.mangila.webshop.product;

import com.github.mangila.webshop.common.util.ValidatorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = {
        ProductValidator.class,
        ValidatorService.class,
        LocalValidatorFactoryBean.class})
class ProductValidatorTest {

    @Autowired
    private ProductValidator validator;

    @Test
    void shouldThrowExceptionWhenProductIsNull() {
        assertThatThrownBy(() -> validator.validate(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Argument for @NotNull parameter 'command'");
    }
}
package com.github.mangila.webshop.product;

import com.github.mangila.webshop.common.util.ValidatorService;
import com.github.mangila.webshop.product.model.ProductCommandType;
import com.github.mangila.webshop.product.model.ProductMutate;
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
        assertThatThrownBy(() -> validator.validateByCommand(ProductCommandType.UPSERT_PRODUCT, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Argument for @NotNull parameter 'productMutate'");
        assertThatThrownBy(() -> validator.validateByCommand(null, ProductMutate.EMPTY))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Argument for @NotNull parameter 'command'");
    }
}
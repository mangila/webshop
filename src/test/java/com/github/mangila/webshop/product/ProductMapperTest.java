package com.github.mangila.webshop.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.webshop.common.util.JsonMapper;
import com.github.mangila.webshop.product.model.ProductEntity;
import com.github.mangila.webshop.product.model.ProductMutate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = {
        ProductMapper.class,
        JsonMapper.class,
        ObjectMapper.class})
class ProductMapperTest {

    @Autowired
    private ProductMapper mapper;

    @Test
    @DisplayName("Should throw IAE when mapping null ProductEntity to Product")
    void shouldThrowIAEWhenMappingNullProductEntityToProduct() {
        assertThatThrownBy(() -> mapper.toProduct((ProductEntity) null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Argument for @NotNull parameter 'entity'");
    }

    @Test
    @DisplayName("Should throw IAE when mapping null Product to ProductEntity")
    void shouldThrowIAEWhenMappingNullProductToProductEntity() {
        assertThatThrownBy(() -> mapper.toEntity((ProductMutate) null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Argument for @NotNull parameter 'mutate'");
    }
}
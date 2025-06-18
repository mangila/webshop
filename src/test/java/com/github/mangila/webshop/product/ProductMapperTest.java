package com.github.mangila.webshop.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.webshop.common.util.JsonMapper;
import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.product.model.ProductEntity;
import com.github.mangila.webshop.product.model.ProductMutate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = {
        ProductMapper.class,
        JsonMapper.class,
        ObjectMapper.class})
class ProductMapperTest {

    @Autowired
    private ProductMapper mapper;

    @Test
    @DisplayName("Should accept ProductMutate with all null fields")
    void shouldAcceptProductMutateWithAllNullFields() {
        var emptyMutate = ProductMutate.EMPTY;
        assertThatCode(() -> {
            var product = mapper.toProduct(emptyMutate);
            assertThat(product).isNotNull();
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should throw IAE when mapping null ProductMutate to Product")
    void shouldThrowIAEWhenMappingNullProductMutateToProduct() {
        assertThatThrownBy(() -> mapper.toProduct((ProductMutate) null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Argument for @NotNull parameter 'mutate'");
    }

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
        assertThatThrownBy(() -> mapper.toEntity((Product) null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Argument for @NotNull parameter 'product'");
    }

    @Test
    @DisplayName("Should throw IAE when mapping null Product to ProductDTO")
    void shouldThrowIAEWhenMappingNullProductToProductDTO() {
        assertThatThrownBy(() -> mapper.toDto((Product) null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Argument for @NotNull parameter 'product'");
    }
}
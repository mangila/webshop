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
        var mutate = new ProductMutate(null,
                null,
                null,
                null,
                null,
                null,
                null);
        assertThatCode(() -> {
            var product = mapper.toProduct(mutate);
            assertThat(product).isNotNull();
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should throw NPE when mapping null ProductMutate to Product")
    void shouldThrowNPEWhenMappingNullProductMutateToProduct() {
        assertThatThrownBy(() -> mapper.toProduct((ProductMutate) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("ProductMutate must not be null");
    }

    @Test
    @DisplayName("Should throw NPE when mapping null ProductEntity to Product")
    void shouldThrowNPEWhenMappingNullProductEntityToProduct() {
        assertThatThrownBy(() -> mapper.toProduct((ProductEntity) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("ProductEntity must not be null");
    }

    @Test
    @DisplayName("Should throw NPE when mapping null Product to ProductEntity")
    void shouldThrowNPEWhenMappingNullProductToProductEntity() {
        assertThatThrownBy(() -> mapper.toEntity((Product) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Product must not be null");
    }

    @Test
    @DisplayName("Should throw NPE when mapping null Product to ProductDTO")
    void shouldThrowNPEWhenMappingNullProductToProductDTO() {
        assertThatThrownBy(() -> mapper.toDto((Product) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Product must not be null");
    }
}
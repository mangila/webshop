package com.github.mangila.webshop.product;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.webshop.common.util.JsonMapper;
import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.product.model.ProductCommand;
import com.github.mangila.webshop.product.model.ProductCommandType;
import com.github.mangila.webshop.product.model.ProductEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.DataClassRowMapper;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = {
        ProductMapper.class,
        JsonMapper.class,
        ObjectMapper.class})
class ProductMapperTest {

    @Autowired
    private ProductMapper mapper;

    @Autowired
    private JsonMapper jsonMapper;

    @Test
    @DisplayName("Should throw NPE when mapping null ProductEntity to Product")
    void shouldThrowNPEWhenMappingNullProductEntityToProduct() {
        assertThatThrownBy(() -> mapper.toProduct((ProductEntity) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Cannot invoke");
    }

    @Test
    @DisplayName("Should throw NPE when mapping null ProductCommand to ProductEntity")
    void shouldThrowNPEWhenMappingNullProductCommandToProductEntity() {
        assertThatThrownBy(() -> mapper.toEntity((ProductCommand) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Cannot invoke");
    }

    @Test
    @DisplayName("Should correctly map ProductEntity to Product")
    void shouldCorrectlyMapProductEntityToProduct() {
        // Arrange
        Instant now = Instant.now();
        Timestamp timestamp = Timestamp.from(now);
        String attributes = "{\"color\":\"red\",\"size\":\"medium\"}";
        ProductEntity entity = new ProductEntity(
                "prod123",
                "Test Product",
                new BigDecimal("99.99"),
                timestamp,
                timestamp,
                attributes
        );

        // Act
        Product product = mapper.toProduct(entity);

        // Assert
        assertThat(product.id()).isEqualTo("prod123");
        assertThat(product.name()).isEqualTo("Test Product");
        assertThat(product.price()).isEqualTo(new BigDecimal("99.99"));
        assertThat(product.created()).isEqualTo(now);
        assertThat(product.updated()).isEqualTo(now);

        JsonNode attributesNode = product.attributes();
        assertThat(attributesNode.get("color").asText()).isEqualTo("red");
        assertThat(attributesNode.get("size").asText()).isEqualTo("medium");
    }

    @Test
    @DisplayName("Should correctly map ProductCommand to ProductEntity")
    void shouldCorrectlyMapProductCommandToProductEntity() {
        // Arrange
        String attributes = "{\"color\":\"blue\",\"size\":\"large\"}";
        ProductCommand command = new ProductCommand(
                ProductCommandType.UPSERT_PRODUCT,
                "prod456",
                "New Product",
                new BigDecimal("149.99"),
                attributes
        );

        // Act
        ProductEntity entity = mapper.toEntity(command);

        // Assert
        assertThat(entity.id()).isEqualTo("prod456");
        assertThat(entity.name()).isEqualTo("New Product");
        assertThat(entity.price()).isEqualTo(new BigDecimal("149.99"));
        assertThat(entity.created()).isNull();
        assertThat(entity.updated()).isNull();
        assertThat(entity.attributes()).isEqualTo(attributes);
    }

    @Test
    @DisplayName("Should return DataClassRowMapper for ProductEntity")
    void shouldReturnDataClassRowMapperForProductEntity() {
        // Act
        DataClassRowMapper<ProductEntity> rowMapper = mapper.getRowMapper();

        // Assert
        assertThat(rowMapper).isNotNull();
        assertThat(rowMapper.getMappedClass()).isEqualTo(ProductEntity.class);
    }
}

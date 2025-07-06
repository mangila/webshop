package com.github.mangila.webshop.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.product.application.cqrs.ProductInsertCommand;
import com.github.mangila.webshop.product.domain.ProductUnit;
import org.springframework.boot.test.context.TestComponent;

import java.math.BigDecimal;

@TestComponent
public class ProductTestUtil {

    public static final String API_V1_PRODUCT_COMMAND_INSERT = "/api/v1/product/command/insert";
    public static final String API_V1_PRODUCT_COMMAND_DELETE = "/api/v1/product/command/delete";

    public static class TestProductInsertCommandBuilder {

        final String defaultName = "Test Product XYZ-10";
        final BigDecimal defaultPrice = new BigDecimal("19.99");
        final ObjectNode defaultAttributes = new ObjectMapper().createObjectNode();
        final ProductUnit defaultUnit = ProductUnit.PIECE;

        private String name = defaultName;
        private BigDecimal price = defaultPrice;
        private ObjectNode attributes = defaultAttributes;
        private ProductUnit unit = defaultUnit;

        public TestProductInsertCommandBuilder() {
        }

        public TestProductInsertCommandBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public TestProductInsertCommandBuilder withPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public TestProductInsertCommandBuilder withAttributes(ObjectNode attributes) {
            this.attributes = attributes;
            return this;
        }

        public TestProductInsertCommandBuilder withUnit(ProductUnit unit) {
            this.unit = unit;
            return this;
        }

        public ProductInsertCommand build() {
            return new ProductInsertCommand(name, price, attributes, unit);
        }

        public ProductInsertCommand buildDefault() {
            return new ProductInsertCommand(defaultName, defaultPrice, defaultAttributes, defaultUnit);
        }
    }
}

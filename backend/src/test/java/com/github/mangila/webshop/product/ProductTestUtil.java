package com.github.mangila.webshop.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.product.domain.command.ProductInsertCommand;
import com.github.mangila.webshop.product.domain.model.ProductName;
import com.github.mangila.webshop.product.domain.model.ProductPrice;
import com.github.mangila.webshop.product.domain.model.ProductUnit;
import org.springframework.boot.test.context.TestComponent;

import java.math.BigDecimal;

@TestComponent
public class ProductTestUtil {

    public static final String API_V1_PRODUCT_COMMAND_INSERT = "/api/v1/product/command/insert";
    public static final String API_V1_PRODUCT_COMMAND_DELETE = "/api/v1/product/command/delete";

    public static class TestProductInsertCommandBuilder {

        private ProductName name = new ProductName("Test Product");
        private ProductPrice price = new ProductPrice(new BigDecimal("19.99"));
        private ObjectNode attributes = new ObjectMapper().createObjectNode();
        private ProductUnit unit = ProductUnit.PIECE;

        public TestProductInsertCommandBuilder() {
        }

        public TestProductInsertCommandBuilder withName(ProductName name) {
            this.name = name;
            return this;
        }

        public TestProductInsertCommandBuilder withPrice(ProductPrice price) {
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
            return new ProductInsertCommand(
                    new ProductName("Test Product"),
                    new ProductPrice(new BigDecimal("19.99")),
                    new ObjectMapper().createObjectNode(),
                    ProductUnit.PIECE);
        }
    }
}

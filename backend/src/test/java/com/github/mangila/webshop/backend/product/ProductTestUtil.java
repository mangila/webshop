package com.github.mangila.webshop.backend.product;

import com.github.mangila.webshop.backend.product.command.model.ProductDeleteCommand;
import com.github.mangila.webshop.backend.product.command.model.ProductUpsertCommand;

import java.math.BigDecimal;

public final class ProductTestUtil {

    public static final String API_V1_PRODUCT_COMMAND_UPSERT = "/api/v1/product/command/upsert";
    public static final String API_V1_PRODUCT_COMMAND_DELETE = "/api/v1/product/command/delete";

    private static final String DUMMY_PRODUCT_ID = "dummy-product-id";
    private static final String DUMMY_PRODUCT_NAME = "Dummy Product Name";
    private static final BigDecimal DUMMY_PRODUCT_PRICE = new BigDecimal("19.99");
    // language=JSON
    private static final String DUMMY_PRODUCT_ATTRIBUTES = "{\"size\":\"medium\",\"color\":\"red\"}";

    private ProductTestUtil() {
    }

    public static String dummyProductId() {
        return DUMMY_PRODUCT_ID;
    }

    public static String dummyProductName() {
        return DUMMY_PRODUCT_NAME;
    }

    public static BigDecimal dummyProductPrice() {
        return DUMMY_PRODUCT_PRICE;
    }

    public static String dummyProductAttributes() {
        return DUMMY_PRODUCT_ATTRIBUTES;
    }

    public static ProductUpsertCommand dummyProductUpsertCommand() {
        return new ProductUpsertCommand(
                DUMMY_PRODUCT_ID,
                DUMMY_PRODUCT_NAME,
                DUMMY_PRODUCT_PRICE,
                DUMMY_PRODUCT_ATTRIBUTES
        );
    }

    public static ProductDeleteCommand dummyProductDeleteCommand() {
        return new ProductDeleteCommand(DUMMY_PRODUCT_ID);
    }
}

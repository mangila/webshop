package com.github.mangila.webshop.product;

import com.github.mangila.webshop.common.util.ValidationUtils;
import com.github.mangila.webshop.product.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductValidator {

    public void ensureRequiredFields(Product product) {
        ensureProductId(product);
        ValidationUtils.ensureNotNull(product.getName(), "name must not be null");
        ValidationUtils.ensureNotNull(product.getPrice(), "price must not be null");
    }

    public void ensureProductId(Product product) {
        ValidationUtils.ensureNotNull(product, "product must not be null");
        ValidationUtils.ensureNotNull(product.getId(), "id must not be null");
    }

    public void ensurePrice(Product product) {
        ensureProductId(product);
        ValidationUtils.ensureNotNull(product.getPrice(), "price must not be null");
    }
}


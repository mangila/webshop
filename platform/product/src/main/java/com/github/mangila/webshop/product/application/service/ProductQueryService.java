package com.github.mangila.webshop.product.application.service;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductQueryRepository;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import com.github.mangila.webshop.shared.util.Ensure;
import com.github.mangila.webshop.shared.util.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ProductQueryService {

    private final ProductQueryRepository repository;

    public ProductQueryService(ProductQueryRepository repository) {
        this.repository = repository;
    }

    public Product findByIdOrThrow(ProductId productId) {
        Ensure.notNull(productId, "ProductId cannot be null");
        return repository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: %s".formatted(productId.value()),
                        Product.class));
    }
}

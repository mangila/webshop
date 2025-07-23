package com.github.mangila.webshop.product.application.service;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductQueryRepository;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import com.github.mangila.webshop.shared.exception.CqrsException;
import com.github.mangila.webshop.shared.model.CqrsOperation;
import org.springframework.stereotype.Service;

@Service
public class ProductQueryService {

    private final ProductQueryRepository repository;

    public ProductQueryService(ProductQueryRepository repository) {
        this.repository = repository;
    }

    public Product findByIdOrThrow(ProductId productId) throws CqrsException {
        return repository.findById(productId)
                .orElseThrow(() -> new CqrsException(
                        "Product not found with id: %s".formatted(productId.value()),
                        CqrsOperation.QUERY,
                        Product.class));
    }
}

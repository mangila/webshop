package com.github.mangila.webshop.product.application.service;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductQueryRepository;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import com.github.mangila.webshop.shared.exception.CqrsException;
import org.springframework.stereotype.Service;

@Service
public class ProductQueryService {

    private final ProductQueryRepository repository;

    public ProductQueryService(ProductQueryRepository repository) {
        this.repository = repository;
    }

    public Product findByIdOrThrow(ProductId productId) throws CqrsException {
        return repository.findByIdOrThrow(productId);
    }
}

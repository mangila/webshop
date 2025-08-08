package com.github.mangila.webshop.product.application.service;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductQueryRepository;
import com.github.mangila.webshop.product.domain.cqrs.FindProductQuery;
import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ProductQueryService {

    private final ProductQueryRepository repository;

    public ProductQueryService(ProductQueryRepository repository) {
        this.repository = repository;
    }

    public Function<FindProductQuery, Product> findByIdOrThrow = this::findByIdOrThrow;

    public Product findByIdOrThrow(FindProductQuery query) {
        Ensure.notNull(query, FindProductQuery.class);
        return repository.findById(query)
                .orElseThrow(() -> new ResourceNotFoundException(Product.class, query));
    }
}

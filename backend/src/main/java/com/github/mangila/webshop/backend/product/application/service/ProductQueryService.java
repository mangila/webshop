package com.github.mangila.webshop.backend.product.application.service;

import com.github.mangila.webshop.backend.common.exception.QueryException;
import com.github.mangila.webshop.backend.product.domain.model.Product;
import com.github.mangila.webshop.backend.product.domain.query.ProductByIdQuery;
import com.github.mangila.webshop.backend.product.infrastructure.ProductQueryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ProductQueryService {

    private final ProductQueryRepository queryRepository;

    public ProductQueryService(ProductQueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    public Product findById(ProductByIdQuery query) {
        var result = queryRepository.findById(query.id());
        return result.orElseThrow(() -> new QueryException(
                String.format("id not found: '%s'", query.id()),
                query.getClass(),
                Product.class,
                HttpStatus.NOT_FOUND));
    }
}

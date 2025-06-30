package com.github.mangila.webshop.backend.product.application.service;

import com.github.mangila.webshop.backend.common.exception.QueryException;
import com.github.mangila.webshop.backend.product.domain.ProductDomain;
import com.github.mangila.webshop.backend.product.infrastructure.ProductQueryRepository;
import com.github.mangila.webshop.backend.product.domain.query.ProductByIdQuery;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ProductQueryService {

    private final ProductQueryRepository queryRepository;

    public ProductQueryService(ProductQueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    public ProductDomain findById(ProductByIdQuery query) {
        var result = queryRepository.findById(query);
        return result.orElseThrow(() -> new QueryException(
                String.format("id not found: '%s'", query.id()),
                query.getClass(),
                ProductDomain.class,
                HttpStatus.NOT_FOUND));
    }
}

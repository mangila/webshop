package com.github.mangila.webshop.backend.product.application.service;

import com.github.mangila.webshop.backend.common.error.exception.QueryException;
import com.github.mangila.webshop.backend.product.application.gateway.ProductRepositoryGateway;
import com.github.mangila.webshop.backend.product.domain.model.Product;
import com.github.mangila.webshop.backend.product.domain.query.ProductByIdQuery;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ProductQueryService {

    private final ProductRepositoryGateway repositoryGateway;

    public ProductQueryService(ProductRepositoryGateway repositoryGateway) {
        this.repositoryGateway = repositoryGateway;
    }

    public Product findById(ProductByIdQuery query) {
        var result = repositoryGateway.query().findById(query.id());
        return result.orElseThrow(() -> new QueryException(
                String.format("id not found: '%s'", query.id()),
                query.getClass(),
                Product.class,
                HttpStatus.NOT_FOUND));
    }
}

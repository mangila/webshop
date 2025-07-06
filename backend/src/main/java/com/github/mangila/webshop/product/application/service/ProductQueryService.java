package com.github.mangila.webshop.product.application.service;

import com.github.mangila.webshop.product.application.cqrs.ProductByIdQuery;
import com.github.mangila.webshop.product.application.dto.ProductDto;
import com.github.mangila.webshop.product.application.gateway.ProductMapperGateway;
import com.github.mangila.webshop.product.application.gateway.ProductRepositoryGateway;
import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductId;
import com.github.mangila.webshop.shared.domain.common.CqrsOperation;
import com.github.mangila.webshop.shared.domain.exception.CqrsException;
import org.springframework.stereotype.Service;

@Service
public class ProductQueryService {

    private final ProductMapperGateway mapper;
    private final ProductRepositoryGateway repository;

    public ProductQueryService(ProductMapperGateway mapper,
                               ProductRepositoryGateway repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    public ProductDto findById(ProductByIdQuery query) {
        Product product = repository.query()
                .findById(new ProductId(query.id()))
                .orElseThrow(() -> new CqrsException(
                        String.format("value not found: '%s'", query.id()),
                        CqrsOperation.QUERY,
                        Product.class
                ));
        return mapper.dto().toDto(product);
    }

    public boolean existsById(ProductId productId) {
        return repository.query().existsById(productId);
    }
}

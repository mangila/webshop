package com.github.mangila.webshop.product.application.service;

import com.github.mangila.webshop.product.application.cqrs.ProductIdQuery;
import com.github.mangila.webshop.product.application.dto.ProductDto;
import com.github.mangila.webshop.product.application.gateway.ProductMapperGateway;
import com.github.mangila.webshop.product.application.gateway.ProductRepositoryGateway;
import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductId;
import com.github.mangila.webshop.shared.domain.common.CqrsOperation;
import com.github.mangila.webshop.shared.domain.exception.CqrsException;
import io.vavr.collection.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProductQueryService {

    private static final Logger log = LoggerFactory.getLogger(ProductQueryService.class);
    private final ProductMapperGateway mapper;
    private final ProductRepositoryGateway repository;

    public ProductQueryService(ProductMapperGateway mapper,
                               ProductRepositoryGateway repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    public ProductDto findById(ProductIdQuery query) {
        return Stream.of(query)
                .peek(q -> log.debug("findById: {}", q.value()))
                .map(mapper.query()::toDomain)
                .map(repository.query()::findById)
                .map(product -> {
                    if (product.isEmpty()) {
                        throw new CqrsException(
                                String.format("value not found: '%s'", query.value()),
                                CqrsOperation.QUERY,
                                Product.class
                        );
                    }
                    return product.get();
                })
                .map(mapper.dto()::toDto)
                .get();
    }

    public boolean existsById(ProductIdQuery query) {
        ProductId productId = mapper.query().toDomain(query);
        return repository.query().existsById(productId);
    }
}

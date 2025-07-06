package com.github.mangila.webshop.product.application.service;

import com.github.mangila.webshop.product.application.cqrs.ProductIdQuery;
import com.github.mangila.webshop.product.application.dto.ProductDto;
import com.github.mangila.webshop.product.application.gateway.ProductMapperGateway;
import com.github.mangila.webshop.product.application.gateway.ProductRepositoryGateway;
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
                .peek(q -> log.debug("Find product by id: {}", q.value()))
                .map(mapper.query()::toDomain)
                .map(repository.query()::findById)
                .map(mapper.dto()::toDto)
                .get();
    }

    public boolean existsById(ProductIdQuery query) {
        return Stream.of(query)
                .peek(q -> log.debug("Check if product exists by id: {}", q.value()))
                .map(mapper.query()::toDomain)
                .map(repository.query()::existsById)
                .get();
    }
}

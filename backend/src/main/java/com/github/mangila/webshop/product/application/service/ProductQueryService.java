package com.github.mangila.webshop.product.application.service;

import com.github.mangila.webshop.product.application.cqrs.ProductIdQuery;
import com.github.mangila.webshop.product.application.dto.ProductDto;
import com.github.mangila.webshop.product.application.gateway.ProductMapperGateway;
import com.github.mangila.webshop.product.application.gateway.ProductRepositoryGateway;
import com.github.mangila.webshop.shared.infrastructure.config.CacheConfig;
import com.github.mangila.webshop.shared.infrastructure.spring.annotation.ObservedService;
import io.vavr.collection.Stream;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;

@Validated
@ObservedService
public class ProductQueryService {

    private static final Logger log = LoggerFactory.getLogger(ProductQueryService.class);
    private final ProductMapperGateway mapper;
    private final ProductRepositoryGateway repository;

    public ProductQueryService(ProductMapperGateway mapper,
                               ProductRepositoryGateway repository) {
        this.mapper = mapper;
        this.repository = repository;
    }


    @Cacheable(value = CacheConfig.LRU, key = "#query.value()")
    public ProductDto findById(@Valid ProductIdQuery query) {
        return Stream.of(query)
                .peek(q -> log.debug("Find product by id: {}", q.value()))
                .map(mapper.query()::toDomain)
                .map(repository.query()::findById)
                .map(mapper.dto()::toDto)
                .get();
    }

    public boolean existsById(@Valid ProductIdQuery query) {
        return Stream.of(query)
                .peek(q -> log.debug("Check if product exists by id: {}", q.value()))
                .map(mapper.query()::toDomain)
                .map(repository.query()::existsById)
                .get();
    }
}

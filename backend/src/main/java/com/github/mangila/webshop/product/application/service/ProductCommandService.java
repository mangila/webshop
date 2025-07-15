package com.github.mangila.webshop.product.application.service;

import com.github.mangila.webshop.product.application.cqrs.ProductIdCommand;
import com.github.mangila.webshop.product.application.cqrs.ProductInsertCommand;
import com.github.mangila.webshop.product.application.dto.ProductDto;
import com.github.mangila.webshop.product.application.gateway.ProductMapperGateway;
import com.github.mangila.webshop.product.application.gateway.ProductRepositoryGateway;
import com.github.mangila.webshop.shared.infrastructure.config.CacheConfig;
import com.github.mangila.webshop.shared.infrastructure.spring.annotation.ObservedService;
import io.vavr.collection.Stream;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
@ObservedService
public class ProductCommandService {

    private static final Logger log = LoggerFactory.getLogger(ProductCommandService.class);

    private final ProductRepositoryGateway productRepositoryGateway;
    private final ProductMapperGateway productMapperGateway;

    public ProductCommandService(ProductRepositoryGateway productRepositoryGateway,
                                 ProductMapperGateway productMapperGateway) {
        this.productRepositoryGateway = productRepositoryGateway;
        this.productMapperGateway = productMapperGateway;
    }

    @Transactional
    @CachePut(value = CacheConfig.LRU,
            cacheManager = "cacheManager",
            condition = "#command != null and #result != null ",
            key = "#result.id()")
    public ProductDto insert(@Valid ProductInsertCommand command) {
        return Stream.of(command)
                .peek(c -> log.debug("Insert product: {}", c))
                .map(productMapperGateway.command()::toDomain)
                .map(productRepositoryGateway.command()::insert)
                .map(productMapperGateway.dto()::toDto)
                .get();
    }

    @Transactional
    @CacheEvict(
            value = CacheConfig.LRU,
            condition = "#command != null and #command.value() != null ",
            key = "#command.value()",
            beforeInvocation = true)
    public void delete(@Valid ProductIdCommand command) {
        Stream.of(command)
                .peek(c -> log.debug("Delete product: {}", c))
                .map(productMapperGateway.command()::toDomain)
                .forEach(productRepositoryGateway.command()::deleteById);
    }
}

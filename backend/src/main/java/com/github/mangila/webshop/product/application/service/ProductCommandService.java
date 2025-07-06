package com.github.mangila.webshop.product.application.service;

import com.github.mangila.webshop.product.application.cqrs.ProductIdCommand;
import com.github.mangila.webshop.product.application.cqrs.ProductInsertCommand;
import com.github.mangila.webshop.product.application.dto.ProductDto;
import com.github.mangila.webshop.product.application.gateway.ProductMapperGateway;
import com.github.mangila.webshop.product.application.gateway.ProductRepositoryGateway;
import io.vavr.collection.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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
    public ProductDto insert(ProductInsertCommand command) {
        return Stream.of(command)
                .map(productMapperGateway.command()::toDomain)
                .map(productRepositoryGateway.command()::insert)
                .map(productMapperGateway.dto()::toDto)
                .get();
    }

    @Transactional
    public void delete(ProductIdCommand command) {
        Stream.of(command)
                .peek(c -> log.debug("Delete product: {}", c))
                .map(productMapperGateway.command()::toDomain)
                .forEach(productRepositoryGateway.command()::deleteById);
    }
}

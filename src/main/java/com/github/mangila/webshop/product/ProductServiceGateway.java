package com.github.mangila.webshop.product;

import com.github.mangila.webshop.product.command.ProductCommandGateway;
import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.product.model.ProductCommand;
import com.github.mangila.webshop.product.query.ProductQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceGateway {

    private final ProductValidator validator;
    private final ProductCommandGateway commandGateway;
    private final ProductQueryService queryService;

    public ProductServiceGateway(ProductValidator validator,
                                 ProductCommandGateway commandGateway,
                                 ProductQueryService queryService) {
        this.validator = validator;
        this.commandGateway = commandGateway;
        this.queryService = queryService;
    }

    public Product queryById(String id) {
        return queryService.queryById(id);
    }

    @Transactional
    public Product processCommand(ProductCommand command) {
        validator.validate(command);
        return commandGateway.processCommand(command);
    }
}

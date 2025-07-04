package com.github.mangila.webshop.backend.product.application.gateway;

import com.github.mangila.webshop.backend.product.application.service.ProductCommandService;
import com.github.mangila.webshop.backend.product.application.service.ProductQueryService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceGateway {

    private final ProductCommandService command;
    private final ProductQueryService query;

    public ProductServiceGateway(ProductCommandService command,
                                 ProductQueryService query) {
        this.command = command;
        this.query = query;
    }

    public ProductCommandService command() {
        return command;
    }

    public ProductQueryService query() {
        return query;
    }
}

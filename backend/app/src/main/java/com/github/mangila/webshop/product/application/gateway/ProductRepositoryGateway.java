package com.github.mangila.webshop.product.application.gateway;

import com.github.mangila.webshop.product.domain.ProductCommandRepository;
import com.github.mangila.webshop.product.domain.ProductQueryRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductRepositoryGateway {

    private final ProductCommandRepository command;
    private final ProductQueryRepository query;

    public ProductRepositoryGateway(ProductCommandRepository command,
                                    ProductQueryRepository query) {
        this.command = command;
        this.query = query;
    }

    public ProductCommandRepository command() {
        return command;
    }

    public ProductQueryRepository query() {
        return query;
    }
}

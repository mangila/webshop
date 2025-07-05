package com.github.mangila.webshop.product.application.gateway;

import com.github.mangila.webshop.product.infrastructure.ProductCommandRepository;
import com.github.mangila.webshop.product.infrastructure.ProductQueryRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductRepositoryGateway {

    private final ProductCommandRepository productCommandRepository;
    private final ProductQueryRepository productQueryRepository;

    public ProductRepositoryGateway(ProductCommandRepository productCommandRepository, ProductQueryRepository productQueryRepository) {
        this.productCommandRepository = productCommandRepository;
        this.productQueryRepository = productQueryRepository;
    }

    public ProductCommandRepository command() {
        return productCommandRepository;
    }

    public ProductQueryRepository query() {
        return productQueryRepository;
    }
}

package com.github.mangila.webshop.backend.product.application.gateway;

import com.github.mangila.webshop.backend.product.domain.model.Product;
import com.github.mangila.webshop.backend.product.domain.model.ProductId;
import com.github.mangila.webshop.backend.product.infrastructure.ProductCommandRepository;
import com.github.mangila.webshop.backend.product.infrastructure.ProductQueryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductRepositoryGateway {

    private final ProductCommandRepository productCommandRepository;
    private final ProductQueryRepository productQueryRepository;

    public ProductRepositoryGateway(ProductCommandRepository productCommandRepository, ProductQueryRepository productQueryRepository) {
        this.productCommandRepository = productCommandRepository;
        this.productQueryRepository = productQueryRepository;
    }

    public Optional<Product> findById(ProductId id) {
        return productQueryRepository.findById(id);
    }

    public void delete(Product product) {
        productCommandRepository.delete(product);
    }

    public Product save(Product product) {
        return productCommandRepository.save(product);
    }
}

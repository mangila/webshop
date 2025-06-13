package com.github.mangila.webshop.product;

import com.github.mangila.webshop.product.model.Product;
import org.springframework.stereotype.Service;

@Service
public class ProductQueryService {

    private final ProductRepository repository;

    public ProductQueryService(ProductRepository repository) {
        this.repository = repository;
    }

    public Product queryById(String id) {
        return repository.queryById(id);
    }
}

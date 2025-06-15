package com.github.mangila.webshop.product;

import com.github.mangila.webshop.product.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProductCommandService {

    private static final Logger log = LoggerFactory.getLogger(ProductCommandService.class);

    private final ProductRepository repository;

    public ProductCommandService(ProductRepository repository) {
        this.repository = repository;
    }

    public void createNewProduct(Product product) {
        repository.insertNew(product);
    }

    public void deleteProductById(String id) {
        repository.deleteProduct(id);
    }
}

package com.github.mangila.webshop.product.query;

import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.product.model.ProductNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ProductQueryService {

    private final ProductQueryRepository queryRepository;

    public ProductQueryService(ProductQueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    public Product queryById(String id) {
        var result = queryRepository.queryById(id);
        return result.orElseThrow(() -> new ProductNotFoundException(id));
    }
}

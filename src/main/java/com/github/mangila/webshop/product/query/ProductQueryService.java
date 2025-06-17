package com.github.mangila.webshop.product.query;

import com.github.mangila.webshop.product.ProductMapper;
import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.product.model.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ProductQueryService {

    private final ProductMapper productMapper;
    private final ProductQueryRepository queryRepository;

    public ProductQueryService(ProductMapper productMapper,
                               ProductQueryRepository queryRepository) {
        this.productMapper = productMapper;
        this.queryRepository = queryRepository;
    }

    public Product queryById(String id) {
        var result = queryRepository.queryById(id);
        return result.orElseThrow(() -> new ProductNotFoundException(id));
    }
}

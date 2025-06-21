package com.github.mangila.webshop.backend.product.query;

import com.github.mangila.webshop.backend.common.util.exception.RequestedResourceNotFoundException;
import com.github.mangila.webshop.backend.product.model.Product;
import com.github.mangila.webshop.backend.product.query.model.ProductQueryById;
import org.springframework.stereotype.Service;

@Service
public class ProductQueryService {

    private final ProductQueryRepository queryRepository;

    public ProductQueryService(ProductQueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    public Product queryById(ProductQueryById query) {
        var result = queryRepository.queryById(query);
        return result.orElseThrow(() -> new RequestedResourceNotFoundException(
                Product.class,
                String.format("id not found: '%s'", query.id())));
    }
}

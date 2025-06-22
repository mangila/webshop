package com.github.mangila.webshop.backend.product.query;

import com.github.mangila.webshop.backend.common.util.exception.QueryException;
import com.github.mangila.webshop.backend.product.model.Product;
import com.github.mangila.webshop.backend.product.query.model.ProductByIdQuery;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ProductQueryService {

    private final ProductQueryRepository queryRepository;

    public ProductQueryService(ProductQueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    public Product findById(ProductByIdQuery query) {
        var result = queryRepository.findById(query);
        return result.orElseThrow(() -> new QueryException(
                String.format("id not found: '%s'", query.id()),
                query.getClass(),
                Product.class,
                HttpStatus.NOT_FOUND));
    }
}

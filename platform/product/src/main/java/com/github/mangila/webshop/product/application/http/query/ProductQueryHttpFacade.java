package com.github.mangila.webshop.product.application.http.query;

import com.github.mangila.webshop.product.application.ProductDto;
import com.github.mangila.webshop.product.application.mapper.ProductHttpRequestMapper;
import com.github.mangila.webshop.product.application.mapper.ProductDtoMapper;
import com.github.mangila.webshop.product.application.service.ProductQueryService;
import com.github.mangila.webshop.product.application.http.request.ProductByIdRequest;
import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import com.github.mangila.webshop.shared.model.CacheName;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ProductQueryHttpFacade {

    private final ProductHttpRequestMapper requestMapper;
    private final ProductDtoMapper dtoMapper;
    private final ProductQueryService service;

    public ProductQueryHttpFacade(ProductHttpRequestMapper requestMapper,
                                  ProductDtoMapper dtoMapper,
                                  ProductQueryService service) {
        this.requestMapper = requestMapper;
        this.dtoMapper = dtoMapper;
        this.service = service;
    }

    @Cacheable(value = CacheName.LRU, key = "#request.value()")
    public ProductDto findProductById(@Valid ProductByIdRequest request) {
        ProductId productId = requestMapper.toDomain(request);
        Product product = service.findByIdOrThrow(productId);
        return dtoMapper.toDto(product);
    }
}

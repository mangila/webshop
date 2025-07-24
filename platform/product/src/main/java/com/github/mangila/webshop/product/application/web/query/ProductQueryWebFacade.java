package com.github.mangila.webshop.product.application.web.query;

import com.github.mangila.webshop.product.application.ProductDto;
import com.github.mangila.webshop.product.application.mapper.ProductDtoMapper;
import com.github.mangila.webshop.product.application.mapper.ProductRequestMapper;
import com.github.mangila.webshop.product.application.service.ProductQueryService;
import com.github.mangila.webshop.product.application.web.request.ProductByIdRequest;
import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import com.github.mangila.webshop.shared.annotation.ObservedService;

@ObservedService
public class ProductQueryWebFacade {

    private final ProductRequestMapper requestMapper;
    private final ProductDtoMapper dtoMapper;
    private final ProductQueryService service;

    public ProductQueryWebFacade(ProductRequestMapper requestMapper,
                                 ProductDtoMapper dtoMapper,
                                 ProductQueryService service) {
        this.requestMapper = requestMapper;
        this.dtoMapper = dtoMapper;
        this.service = service;
    }

    public ProductDto findProductById(ProductByIdRequest request) {
        ProductId productId = requestMapper.toQuery(request);
        Product product = service.findByIdOrThrow(productId);
        return dtoMapper.toDto(product);
    }
}

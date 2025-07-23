package com.github.mangila.webshop.product.application.web.query;

import com.github.mangila.webshop.product.application.ProductDto;
import com.github.mangila.webshop.product.application.mapper.ProductDtoMapper;
import com.github.mangila.webshop.product.application.mapper.ProductRequestMapper;
import com.github.mangila.webshop.product.application.service.ProductQueryService;
import com.github.mangila.webshop.product.application.web.request.ProductByIdRequest;
import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import org.springframework.stereotype.Service;

@Service
public class ProductWebQueryService {

    private final ProductRequestMapper mapper;
    private final ProductDtoMapper dtoMapper;
    private final ProductQueryService service;

    public ProductWebQueryService(ProductRequestMapper mapper, ProductDtoMapper dtoMapper,
                                  ProductQueryService service) {
        this.mapper = mapper;
        this.dtoMapper = dtoMapper;
        this.service = service;
    }

    public ProductDto findProductById(ProductByIdRequest request) {
        ProductId productId = mapper.toQuery(request);
        Product product = service.findByIdOrThrow(productId);
        return dtoMapper.toDto(product);
    }
}

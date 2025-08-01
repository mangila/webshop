package com.github.mangila.webshop.product.application.graphql;

import com.github.mangila.webshop.product.application.ProductDto;
import com.github.mangila.webshop.product.application.graphql.input.ProductIdInput;
import com.github.mangila.webshop.product.application.ProductDtoMapper;
import com.github.mangila.webshop.product.application.service.ProductQueryService;
import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import com.github.mangila.webshop.shared.model.CacheName;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ProductQueryFacade {

    private final ProductGraphqlMapper graphqlMapper;
    private final ProductDtoMapper dtoMapper;
    private final ProductQueryService service;

    public ProductQueryFacade(ProductGraphqlMapper graphqlMapper,
                              ProductDtoMapper dtoMapper,
                              ProductQueryService service) {
        this.graphqlMapper = graphqlMapper;
        this.dtoMapper = dtoMapper;
        this.service = service;
    }

    @Cacheable(value = CacheName.LRU, key = "#request.value()")
    public ProductDto findProductById(ProductIdInput request) {
        ProductId productId = graphqlMapper.toDomain(request);
        Product product = service.findByIdOrThrow(productId);
        return dtoMapper.toDto(product);
    }
}

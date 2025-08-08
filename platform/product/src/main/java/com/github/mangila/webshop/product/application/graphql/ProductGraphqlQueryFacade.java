package com.github.mangila.webshop.product.application.graphql;

import com.github.mangila.webshop.product.application.ProductDto;
import com.github.mangila.webshop.product.application.ProductDtoMapper;
import com.github.mangila.webshop.product.application.graphql.input.FindProductInput;
import com.github.mangila.webshop.product.application.service.ProductQueryService;
import com.github.mangila.webshop.product.domain.cqrs.FindProductQuery;
import com.github.mangila.webshop.shared.model.CacheName;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ProductGraphqlQueryFacade {

    private final ProductGraphqlMapper graphqlMapper;
    private final ProductDtoMapper dtoMapper;
    private final ProductQueryService service;

    public ProductGraphqlQueryFacade(ProductGraphqlMapper graphqlMapper,
                                     ProductDtoMapper dtoMapper,
                                     ProductQueryService service) {
        this.graphqlMapper = graphqlMapper;
        this.dtoMapper = dtoMapper;
        this.service = service;
    }

    @Cacheable(value = CacheName.LRU, key = "#request.value()")
    public ProductDto findProductById(FindProductInput request) {
        FindProductQuery query = graphqlMapper.toQuery(request);
        return service.findByIdOrThrow.andThen(dtoMapper::toDto)
                .apply(query);
    }
}

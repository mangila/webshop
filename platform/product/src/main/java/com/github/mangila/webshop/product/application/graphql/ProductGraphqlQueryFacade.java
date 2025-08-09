package com.github.mangila.webshop.product.application.graphql;

import com.github.mangila.webshop.product.application.ProductDto;
import com.github.mangila.webshop.product.application.ProductDtoMapper;
import com.github.mangila.webshop.product.application.action.query.FindProductByIdQueryAction;
import com.github.mangila.webshop.product.application.graphql.input.FindProductByIdInput;
import com.github.mangila.webshop.product.domain.cqrs.FindProductByIdQuery;
import com.github.mangila.webshop.shared.model.CacheName;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ProductGraphqlQueryFacade {

    private final ProductGraphqlMapper graphqlMapper;
    private final ProductDtoMapper dtoMapper;
    private final FindProductByIdQueryAction findProductByIdQueryAction;

    public ProductGraphqlQueryFacade(ProductGraphqlMapper graphqlMapper, ProductDtoMapper dtoMapper, FindProductByIdQueryAction findProductByIdQueryAction) {
        this.graphqlMapper = graphqlMapper;
        this.dtoMapper = dtoMapper;
        this.findProductByIdQueryAction = findProductByIdQueryAction;
    }

    @Cacheable(value = CacheName.LRU, key = "#request.value()")
    public ProductDto findProductById(@Valid FindProductByIdInput request) {
        FindProductByIdQuery query = graphqlMapper.toQuery(request);
        return findProductByIdQueryAction.execute()
                .andThen(dtoMapper::toDto)
                .apply(query);
    }
}

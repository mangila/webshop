package com.github.mangila.webshop.product.application.graphql;

import com.github.mangila.webshop.product.application.graphql.input.ProductIdInput;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import org.springframework.stereotype.Component;

@Component
public class ProductGraphqlMapper {

    public ProductId toDomain(ProductIdInput input) {
        return new ProductId(input.value());
    }

}

package com.github.mangila.webshop.product.application.graphql;

import com.github.mangila.webshop.product.application.graphql.input.FindProductByIdInput;
import com.github.mangila.webshop.product.domain.cqrs.FindProductByIdQuery;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import org.springframework.stereotype.Component;

@Component
public class ProductGraphqlMapper {

    public FindProductByIdQuery toQuery(FindProductByIdInput input) {
        ProductId productId = new ProductId(input.value());
        return new FindProductByIdQuery(productId);
    }

}

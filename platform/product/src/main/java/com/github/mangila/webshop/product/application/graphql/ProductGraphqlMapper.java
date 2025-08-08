package com.github.mangila.webshop.product.application.graphql;

import com.github.mangila.webshop.product.application.graphql.input.ProductIdInput;
import com.github.mangila.webshop.product.domain.cqrs.FindProductQuery;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import org.springframework.stereotype.Component;

@Component
public class ProductGraphqlMapper {

    public FindProductQuery toQuery(ProductIdInput input) {
        ProductId productId = new ProductId(input.value());
        return new FindProductQuery(productId);
    }

}

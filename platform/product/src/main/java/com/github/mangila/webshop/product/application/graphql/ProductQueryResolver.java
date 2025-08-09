package com.github.mangila.webshop.product.application.graphql;

import com.github.mangila.webshop.product.application.ProductDto;
import com.github.mangila.webshop.product.application.graphql.input.FindProductByIdInput;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ProductQueryResolver {

    private final ProductGraphqlQueryFacade facade;

    public ProductQueryResolver(ProductGraphqlQueryFacade facade) {
        this.facade = facade;
    }

    @QueryMapping
    public ProductDto findProductById(@Argument("input") FindProductByIdInput input) {
        return facade.findProductById(input);
    }
}

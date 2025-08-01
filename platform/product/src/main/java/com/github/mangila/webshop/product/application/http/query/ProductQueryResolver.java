package com.github.mangila.webshop.product.application.http.query;

import com.github.mangila.webshop.product.application.ProductDto;
import com.github.mangila.webshop.product.application.http.request.ProductByIdRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ProductQueryResolver {

    private final ProductQueryFacade facade;

    public ProductQueryResolver(ProductQueryFacade facade) {
        this.facade = facade;
    }

    @QueryMapping
    public ProductDto findProductById(@Argument("request") ProductByIdRequest request) {
        return facade.findProductById(request);
    }
}

package com.github.mangila.webshop.product.application.web.query;

import com.github.mangila.webshop.product.application.ProductDto;
import com.github.mangila.webshop.product.application.web.request.ProductByIdRequest;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ProductQueryResolver {

    private final ProductWebQueryService service;

    public ProductQueryResolver(ProductWebQueryService service) {
        this.service = service;
    }

    @QueryMapping
    public ProductDto findProductById(@Argument("request") @Valid ProductByIdRequest request) {
        return service.findProductById(request);
    }
}

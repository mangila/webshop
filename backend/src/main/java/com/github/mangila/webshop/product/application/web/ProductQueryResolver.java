package com.github.mangila.webshop.product.application.web;

import com.github.mangila.webshop.product.application.cqrs.ProductByIdQuery;
import com.github.mangila.webshop.product.application.dto.ProductDto;
import com.github.mangila.webshop.product.application.gateway.ProductServiceGateway;
import com.github.mangila.webshop.product.domain.Product;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ProductQueryResolver {

    private final ProductServiceGateway productServiceGateway;

    public ProductQueryResolver(ProductServiceGateway productServiceGateway) {
        this.productServiceGateway = productServiceGateway;
    }

    @QueryMapping
    public ProductDto findProductById(@Argument("input") @Valid ProductByIdQuery query) {
        return productServiceGateway.query().findById(query);
    }
}
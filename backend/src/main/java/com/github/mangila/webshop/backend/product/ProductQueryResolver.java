package com.github.mangila.webshop.backend.product;

import com.github.mangila.webshop.backend.product.model.Product;
import com.github.mangila.webshop.backend.product.query.model.ProductByIdQuery;
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
    public Product findProductById(@Argument("input") @Valid ProductByIdQuery query) {
        return productServiceGateway.findById(query);
    }
}
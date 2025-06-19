package com.github.mangila.webshop.product.query;

import com.github.mangila.webshop.product.ProductServiceGateway;
import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.product.query.model.ProductQueryById;
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
    public Product queryProductById(@Argument("id") @Valid ProductQueryById query) {
        return productServiceGateway.queryById(query.id());
    }
}
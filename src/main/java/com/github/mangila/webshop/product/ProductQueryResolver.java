package com.github.mangila.webshop.product;

import com.github.mangila.webshop.product.model.Product;
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
    public Product queryProductById(@Argument("id") String id) {
        return productServiceGateway.queryById(id);
    }
}
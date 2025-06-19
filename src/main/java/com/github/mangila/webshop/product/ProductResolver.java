package com.github.mangila.webshop.product;

import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.product.model.ProductCommand;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ProductResolver {

    private final ProductServiceGateway productServiceGateway;

    public ProductResolver(ProductServiceGateway productServiceGateway) {
        this.productServiceGateway = productServiceGateway;
    }

    @QueryMapping
    public Product queryProductById(@Argument("id") String id) {
        return productServiceGateway.queryById(id);
    }

    @MutationMapping
    public Product mutateProduct(@Argument("command") ProductCommand command) {
        return productServiceGateway.processCommand(command);
    }
}
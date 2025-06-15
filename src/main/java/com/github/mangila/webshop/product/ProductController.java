package com.github.mangila.webshop.product;

import com.github.mangila.webshop.product.command.ProductCommandService;
import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.product.model.ProductCommandType;
import com.github.mangila.webshop.product.query.ProductQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final ProductQueryService productQueryService;
    private final ProductCommandService productCommandService;

    public ProductController(ProductQueryService productQueryService,
                             ProductCommandService productCommandService) {
        this.productQueryService = productQueryService;
        this.productCommandService = productCommandService;
    }

    @QueryMapping
    public Product queryProductById(@Argument("input") String id) {
        return productQueryService.queryById(id);
    }

    @MutationMapping
    public Product mutateProduct(
            @Argument("command") ProductCommandType command,
            @Argument("input") Product product) {
        return productCommandService.processCommand(command, product);
    }
}
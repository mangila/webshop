package com.github.mangila.webshop.product;

import com.github.mangila.webshop.product.command.ProductCommandGateway;
import com.github.mangila.webshop.product.model.ProductCommand;
import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.product.query.ProductQueryService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Controller;

@Controller
public class ProductResolver extends DataFetcherExceptionResolverAdapter {

    private final ProductQueryService productQueryService;
    private final ProductCommandGateway productCommandGateway;

    public ProductResolver(ProductQueryService productQueryService,
                           ProductCommandGateway productCommandGateway) {
        this.productQueryService = productQueryService;
        this.productCommandGateway = productCommandGateway;
    }

    @QueryMapping
    public Product queryProductById(@Argument("id") String id) {
        return productQueryService.queryById(id);
    }

    @MutationMapping
    public Product mutateProduct(@Argument("command") ProductCommand command) {
        return productCommandGateway.processCommand(command);
    }
}
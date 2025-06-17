package com.github.mangila.webshop.product;

import com.github.mangila.webshop.product.command.ProductCommandService;
import com.github.mangila.webshop.product.model.ProductCommandType;
import com.github.mangila.webshop.product.model.ProductDto;
import com.github.mangila.webshop.product.model.ProductMutate;
import com.github.mangila.webshop.product.query.ProductQueryService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ProductController {

    private final ProductMapper productMapper;
    private final ProductQueryService productQueryService;
    private final ProductCommandService productCommandService;

    public ProductController(ProductMapper productMapper,
                             ProductQueryService productQueryService,
                             ProductCommandService productCommandService) {
        this.productMapper = productMapper;
        this.productQueryService = productQueryService;
        this.productCommandService = productCommandService;
    }

    @QueryMapping
    public ProductDto queryProductById(@Argument("id") String id) {
        var product = productQueryService.queryById(id);
        return productMapper.toDto(product);
    }

    @MutationMapping
    public ProductDto mutateProduct(
            @Argument("command") ProductCommandType command,
            @Argument("mutate") ProductMutate productMutate) {
        var product = productCommandService.processMutateCommand(command, productMutate);
        return productMapper.toDto(product);
    }
}
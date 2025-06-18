package com.github.mangila.webshop.product;

import com.github.mangila.webshop.product.command.ProductCommandGateway;
import com.github.mangila.webshop.product.model.ProductCommandType;
import com.github.mangila.webshop.product.model.ProductDto;
import com.github.mangila.webshop.product.model.ProductMutate;
import com.github.mangila.webshop.product.model.exception.ProductCommandException;
import com.github.mangila.webshop.product.model.exception.ProductNotFoundException;
import com.github.mangila.webshop.product.query.ProductQueryService;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Controller;

@Controller
public class ProductResolver extends DataFetcherExceptionResolverAdapter {

    private final ProductMapper productMapper;
    private final ProductQueryService productQueryService;
    private final ProductCommandGateway productCommandGateway;

    public ProductResolver(ProductMapper productMapper,
                           ProductQueryService productQueryService,
                           ProductCommandGateway productCommandGateway) {
        this.productMapper = productMapper;
        this.productQueryService = productQueryService;
        this.productCommandGateway = productCommandGateway;
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
        var product = productCommandGateway.processCommand(command, productMutate);
        return productMapper.toDto(product);
    }
}
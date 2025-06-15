package com.github.mangila.webshop.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.mangila.webshop.common.event.Event;
import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.product.model.ProductEventType;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ProductController {

    private final ProductQueryService productQueryService;
    private final ProductEventService productEventService;

    public ProductController(ProductQueryService productQueryService,
                             ProductEventService productEventService) {
        this.productQueryService = productQueryService;
        this.productEventService = productEventService;
    }

    @QueryMapping
    public Product queryProductById(@Argument("input") String id) {
        return productQueryService.queryById(id);
    }

    @MutationMapping
    public Event mutateProduct(
            @Argument("intent") ProductEventType eventType,
            @Argument("input") Product product) throws JsonProcessingException {
        return productEventService.processMutation(eventType, product);
    }
}
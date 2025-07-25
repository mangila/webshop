package com.github.mangila.webshop.product.application.web.query;

import com.github.mangila.webshop.product.application.ProductDto;
import com.github.mangila.webshop.product.application.web.request.ProductByIdRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ProductQueryResolver {

    private final ProductQueryWebFacade webFacade;

    public ProductQueryResolver(ProductQueryWebFacade webFacade) {
        this.webFacade = webFacade;
    }

    @QueryMapping
    public ProductDto findProductById(@Argument("request") ProductByIdRequest request) {
        return webFacade.findProductById(request);
    }
}

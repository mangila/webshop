package com.github.mangila.webshop.product.application.http;

import com.github.mangila.webshop.product.application.ProductDto;
import com.github.mangila.webshop.product.application.http.request.ProductByIdRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ProductQueryResolver {

    private final ProductQueryHttpFacade webFacade;

    public ProductQueryResolver(ProductQueryHttpFacade webFacade) {
        this.webFacade = webFacade;
    }

    @QueryMapping
    public ProductDto findProductById(@Argument("request") ProductByIdRequest request) {
        return webFacade.findProductById(request);
    }
}

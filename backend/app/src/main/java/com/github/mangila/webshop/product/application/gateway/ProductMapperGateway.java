package com.github.mangila.webshop.product.application.gateway;

import com.github.mangila.webshop.product.application.mapper.ProductCommandMapper;
import com.github.mangila.webshop.product.application.mapper.ProductDtoMapper;
import com.github.mangila.webshop.product.application.mapper.ProductQueryMapper;
import com.github.mangila.webshop.product.domain.ProductCommandRepository;
import com.github.mangila.webshop.product.domain.ProductQueryRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductMapperGateway {

    private final ProductCommandMapper command;
    private final ProductQueryMapper query;
    private final ProductDtoMapper dto;

    public ProductMapperGateway(ProductCommandMapper command,
                                ProductQueryMapper query,
                                ProductDtoMapper dto) {
        this.command = command;
        this.query = query;
        this.dto = dto;
    }

    public ProductCommandMapper command() {
        return command;
    }

    public ProductQueryMapper query() {
        return query;
    }

    public ProductDtoMapper dto() {
        return dto;
    }
}

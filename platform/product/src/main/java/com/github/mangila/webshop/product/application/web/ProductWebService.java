package com.github.mangila.webshop.product.application.web;

import com.github.mangila.webshop.identity.application.DomainIdGeneratorService;
import com.github.mangila.webshop.identity.application.NewDomainIdRequest;
import com.github.mangila.webshop.product.application.ProductDto;
import com.github.mangila.webshop.product.application.mapper.ProductDtoMapper;
import com.github.mangila.webshop.product.application.mapper.ProductRequestMapper;
import com.github.mangila.webshop.product.application.service.ProductCommandService;
import com.github.mangila.webshop.product.application.web.request.ProductInsertRequest;
import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.cqrs.ProductInsertCommand;
import com.github.mangila.webshop.shared.model.Domain;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductWebService {

    private final ProductDtoMapper dtoMapper;
    private final ProductRequestMapper webMapper;
    private final ProductCommandService service;
    private final DomainIdGeneratorService idGenerator;

    public ProductWebService(ProductDtoMapper dtoMapper,
                             ProductRequestMapper webMapper,
                             ProductCommandService service,
                             DomainIdGeneratorService idGenerator) {
        this.dtoMapper = dtoMapper;
        this.webMapper = webMapper;
        this.service = service;
        this.idGenerator = idGenerator;
    }

    @Transactional
    public ProductDto insert(ProductInsertRequest request) {
        var id = idGenerator.generate(new NewDomainIdRequest(Domain.from(Product.class)));
        ProductInsertCommand command = webMapper.toCommand(id, request);
        var product = service.insert(command);
        return dtoMapper.toDto(product);
    }
}

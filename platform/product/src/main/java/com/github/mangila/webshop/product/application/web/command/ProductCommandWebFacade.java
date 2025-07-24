package com.github.mangila.webshop.product.application.web.command;

import com.github.mangila.webshop.identity.application.DomainIdGeneratorService;
import com.github.mangila.webshop.identity.application.NewDomainIdRequest;
import com.github.mangila.webshop.product.application.ProductDto;
import com.github.mangila.webshop.product.application.mapper.ProductDtoMapper;
import com.github.mangila.webshop.product.application.mapper.ProductRequestMapper;
import com.github.mangila.webshop.product.application.service.ProductCommandService;
import com.github.mangila.webshop.product.application.web.request.ProductInsertRequest;
import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.cqrs.ProductInsertCommand;
import com.github.mangila.webshop.shared.annotation.ObservedService;
import com.github.mangila.webshop.shared.model.Domain;
import org.springframework.transaction.annotation.Transactional;

@ObservedService
public class ProductCommandWebFacade {

    private final ProductDtoMapper dtoMapper;
    private final ProductRequestMapper requestMapper;
    private final ProductCommandService commandService;
    private final DomainIdGeneratorService idGenerator;

    public ProductCommandWebFacade(ProductDtoMapper dtoMapper,
                                   ProductRequestMapper requestMapper,
                                   ProductCommandService commandService,
                                   DomainIdGeneratorService idGenerator) {
        this.dtoMapper = dtoMapper;
        this.requestMapper = requestMapper;
        this.commandService = commandService;
        this.idGenerator = idGenerator;
    }

    @Transactional
    public ProductDto insert(ProductInsertRequest request) {
        var id = idGenerator.generate(new NewDomainIdRequest(Domain.from(Product.class)));
        ProductInsertCommand command = requestMapper.toCommand(id, request);
        Product product = commandService.insert(command);
        return dtoMapper.toDto(product);
    }
}

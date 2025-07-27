package com.github.mangila.webshop.product.application.web;

import com.github.mangila.webshop.identity.application.DomainIdFacade;
import com.github.mangila.webshop.identity.application.NewDomainIdRequest;
import com.github.mangila.webshop.product.application.ProductDto;
import com.github.mangila.webshop.product.application.mapper.ProductDtoMapper;
import com.github.mangila.webshop.product.application.mapper.ProductRequestMapper;
import com.github.mangila.webshop.product.application.service.ProductCommandService;
import com.github.mangila.webshop.product.application.web.request.ProductByIdRequest;
import com.github.mangila.webshop.product.application.web.request.ProductInsertRequest;
import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.cqrs.ProductInsertCommand;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import com.github.mangila.webshop.shared.model.Domain;
import com.github.mangila.webshop.shared.registry.RegistryService;
import com.github.mangila.webshop.shared.model.CacheName;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@Validated
public class ProductCommandWebFacade {

    private final ProductDtoMapper dtoMapper;
    private final ProductRequestMapper requestMapper;
    private final ProductCommandService commandService;
    private final DomainIdFacade domainIdFacade;
    private final RegistryService registryService;

    public ProductCommandWebFacade(ProductDtoMapper dtoMapper,
                                   ProductRequestMapper requestMapper,
                                   ProductCommandService commandService,
                                   DomainIdFacade domainIdFacade,
                                   RegistryService registryService) {
        this.dtoMapper = dtoMapper;
        this.requestMapper = requestMapper;
        this.commandService = commandService;
        this.domainIdFacade = domainIdFacade;
        this.registryService = registryService;
    }

    @Transactional
    @CachePut(value = CacheName.LRU, key = "#result.id()")
    public ProductDto insert(@Valid ProductInsertRequest request) {
        var domain = new Domain(Product.class, registryService);
        var domainIdRequest = new NewDomainIdRequest(domain);
        UUID id = domainIdFacade.generate(domainIdRequest);
        ProductInsertCommand command = requestMapper.toCommand(id, request);
        Product product = commandService.insert(command);
        return dtoMapper.toDto(product);
    }

    @Transactional
    @CacheEvict(value = CacheName.LRU, key = "#a0.value()")
    public void deleteById(@Valid ProductByIdRequest request) {
        ProductId productId = requestMapper.toDomain(request);
        commandService.deleteByIdOrThrow(productId);
    }
}

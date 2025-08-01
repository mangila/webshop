package com.github.mangila.webshop.product.application.http.command;

import com.github.mangila.webshop.identity.application.IdentityService;
import com.github.mangila.webshop.identity.domain.Identity;
import com.github.mangila.webshop.identity.domain.cqrs.NewIdentityCommand;
import com.github.mangila.webshop.product.application.ProductDto;
import com.github.mangila.webshop.product.application.mapper.ProductHttpRequestMapper;
import com.github.mangila.webshop.product.application.mapper.ProductDtoMapper;
import com.github.mangila.webshop.product.application.service.ProductCommandService;
import com.github.mangila.webshop.product.application.http.request.ProductByIdRequest;
import com.github.mangila.webshop.product.application.http.request.ProductInsertRequest;
import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.cqrs.ProductInsertCommand;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import com.github.mangila.webshop.shared.model.CacheName;
import com.github.mangila.webshop.shared.model.Domain;
import com.github.mangila.webshop.shared.registry.DomainRegistry;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ProductCommandHttpFacade {

    private final ProductDtoMapper dtoMapper;
    private final ProductHttpRequestMapper requestMapper;
    private final ProductCommandService commandService;
    private final IdentityService identityService;
    private final DomainRegistry domainRegistry;

    public ProductCommandHttpFacade(ProductDtoMapper dtoMapper,
                                    ProductHttpRequestMapper requestMapper,
                                    ProductCommandService commandService, IdentityService identityService,
                                    DomainRegistry domainRegistry) {
        this.dtoMapper = dtoMapper;
        this.requestMapper = requestMapper;
        this.commandService = commandService;
        this.identityService = identityService;
        this.domainRegistry = domainRegistry;
    }

    @Transactional
    @CachePut(value = CacheName.LRU, key = "#result.id()")
    public ProductDto insert(@Valid ProductInsertRequest request) {
        Domain domain = new Domain(Product.class, domainRegistry);
        Identity identity = identityService.generate(new NewIdentityCommand(domain));
        ProductInsertCommand command = requestMapper.toCommand(identity.id(), request);
        Product product = commandService.insert(command);
        return dtoMapper.toDto(product);
    }

    @Transactional
    @CacheEvict(value = CacheName.LRU, key = "#request.value()")
    public void deleteById(@Valid ProductByIdRequest request) {
        ProductId productId = requestMapper.toDomain(request);
        commandService.deleteByIdOrThrow(productId);
    }
}

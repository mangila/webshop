package com.github.mangila.webshop.product.application.rest;

import com.github.mangila.webshop.product.application.rest.request.CreateProductRequest;
import com.github.mangila.webshop.product.application.rest.request.UpdateProductStatusRequest;
import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.cqrs.CreateProductCommand;
import com.github.mangila.webshop.product.domain.cqrs.UpdateProductStatusCommand;
import com.github.mangila.webshop.product.domain.primitive.ProductAttributes;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import com.github.mangila.webshop.product.domain.primitive.ProductName;
import com.github.mangila.webshop.product.domain.types.ProductStatusType;
import com.github.mangila.webshop.shared.identity.application.IdentityService;
import com.github.mangila.webshop.shared.identity.domain.Identity;
import com.github.mangila.webshop.shared.identity.domain.cqrs.NewIdentityCommand;
import com.github.mangila.webshop.shared.model.Domain;
import com.github.mangila.webshop.shared.registry.DomainRegistry;
import org.springframework.stereotype.Component;

@Component
public class ProductRestMapper {

    private final DomainRegistry domainRegistry;
    private final IdentityService identityService;

    public ProductRestMapper(DomainRegistry domainRegistry,
                             IdentityService identityService) {
        this.domainRegistry = domainRegistry;
        this.identityService = identityService;
    }

    public CreateProductCommand toCommand(CreateProductRequest request) {
        Domain domain = new Domain(Product.class, domainRegistry);
        Identity identity = identityService.generate(new NewIdentityCommand(domain));
        return new CreateProductCommand(
                new ProductId(identity.id()),
                new ProductName(request.name()),
                new ProductAttributes(request.attributes()),
                ProductStatusType.INACTIVE
        );
    }

    public UpdateProductStatusCommand toCommand(UpdateProductStatusRequest request) {
        ProductId productId = new ProductId(request.value());
        return new UpdateProductStatusCommand(productId, request.status());
    }
}

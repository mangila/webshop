package com.github.mangila.webshop.product.infrastructure.registry;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.event.ProductEvent;
import com.github.mangila.webshop.shared.registry.RegistryService;
import com.github.mangila.webshop.shared.registry.model.Domain;
import com.github.mangila.webshop.shared.registry.model.Event;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.EnumSet;

@Configuration
public class ProductRegistryConfig {
    private final RegistryService registryService;

    public ProductRegistryConfig(RegistryService registryService) {
        this.registryService = registryService;
    }

    @PostConstruct
    public void init() {
        final Class<Product> clazz = Product.class;
        final Domain domain = Domain.createNew(clazz);
        registryService.registerDomain(domain);
        EnumSet.allOf(ProductEvent.class)
                .stream()
                .map(Event::createNew)
                .forEach(registryService::registerEvent);
    }
}

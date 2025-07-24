package com.github.mangila.webshop.product.infrastructure.registry;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.event.ProductEvent;
import com.github.mangila.webshop.shared.registry.RegistryService;
import com.github.mangila.webshop.shared.registry.model.Domain;
import com.github.mangila.webshop.shared.registry.model.Event;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.EnumSet;

@Configuration
public class ProductRegistryConfig {

    private static final Logger log = LoggerFactory.getLogger(ProductRegistryConfig.class);

    private final RegistryService registryService;

    public ProductRegistryConfig(RegistryService registryService) {
        this.registryService = registryService;
    }

    @PostConstruct
    public void init() {
        final Domain domain = Domain.from(Product.class);
        registryService.registerDomain(domain);
        EnumSet.allOf(ProductEvent.class)
                .stream()
                .map(Event::from)
                .forEach(registryService::registerEvent);
    }
}

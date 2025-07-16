package com.github.mangila.webshop.product.application.config;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.infrastructure.event.ProductEvent;
import com.github.mangila.webshop.shared.application.registry.Domain;
import com.github.mangila.webshop.shared.application.registry.Event;
import com.github.mangila.webshop.shared.application.registry.RegistryService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.EnumSet;

@Configuration
public class ProductDomainRegistryConfig {

    private static final Logger log = LoggerFactory.getLogger(ProductDomainRegistryConfig.class);

    private final RegistryService registryService;

    public ProductDomainRegistryConfig(RegistryService registryService) {
        this.registryService = registryService;
    }

    @PostConstruct
    void init() {
        registerProductDomain();
        registerProductEvents();
    }

    void registerProductDomain() {
        final Domain domain = Domain.from(Product.class);
        log.info("Registering domain: {}", domain.value());
        registryService.register(domain);
    }

    void registerProductEvents() {
        EnumSet.allOf(ProductEvent.class)
                .stream()
                .map(Event::from)
                .forEach(event -> {
                    log.info("Registering event: {}", event.value());
                    registryService.register(event);
                });
    }
}

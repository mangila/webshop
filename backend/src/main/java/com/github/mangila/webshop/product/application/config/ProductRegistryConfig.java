package com.github.mangila.webshop.product.application.config;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.infrastructure.event.ProductEvent;
import com.github.mangila.webshop.shared.application.registry.DomainKey;
import com.github.mangila.webshop.shared.application.registry.EventKey;
import com.github.mangila.webshop.shared.application.registry.RegistryService;
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
    void init() {
        registerProductDomain();
        registerProductEvents();
    }

    void registerProductDomain() {
        final DomainKey domainKey = DomainKey.create(Product.class);
        log.info("Registering domain: {}", domainKey.value());
        registryService.registerDomain(domainKey, domainKey.value());
    }

    void registerProductEvents() {
        EnumSet.allOf(ProductEvent.class)
                .stream()
                .map(EventKey::create)
                .forEach(eventKey -> {
                    log.info("Registering event: {}", eventKey.value());
                    registryService.registerEvent(eventKey, eventKey.value());
                });
    }
}

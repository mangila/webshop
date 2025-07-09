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

    public static final DomainKey PRODUCT_DOMAIN_KEY = DomainKey.from(Product.class);

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
        log.info("Registering domain: {}", PRODUCT_DOMAIN_KEY.value());
        registryService.registerDomain(PRODUCT_DOMAIN_KEY, PRODUCT_DOMAIN_KEY.value());
    }

    void registerProductEvents() {
        EnumSet.allOf(ProductEvent.class)
                .stream()
                .map(EventKey::from)
                .forEach(eventKey -> {
                    log.info("Registering event: {}", eventKey.value());
                    registryService.registerEvent(eventKey, eventKey.value());
                });
    }
}

package com.github.mangila.webshop.product.infrastructure.registry;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.event.ProductEvent;
import com.github.mangila.webshop.shared.model.Domain;
import com.github.mangila.webshop.shared.model.Event;
import com.github.mangila.webshop.shared.registry.DomainRegistry;
import com.github.mangila.webshop.shared.registry.EventRegistry;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.EnumSet;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class ProductRegistryConfig {

    private static final Logger log = LoggerFactory.getLogger(ProductRegistryConfig.class);
    private final DomainRegistry domainRegistry;
    private final EventRegistry eventRegistry;

    public ProductRegistryConfig(DomainRegistry domainRegistry, EventRegistry eventRegistry) {
        this.domainRegistry = domainRegistry;
        this.eventRegistry = eventRegistry;
    }

    @PostConstruct
    public void init() {
        final Class<Product> clazz = Product.class;
        final Domain domain = new Domain(clazz);
        log.info("Registering domain {}", domain.value());
        domainRegistry.register(domain, domain.value());
        EnumSet.allOf(ProductEvent.class)
                .stream()
                .map(Event::new)
                .peek(event -> log.info("Registering event {} for domain {}", event.value(), domain.value()))
                .collect(Collectors.toMap(Function.identity(), Event::value))
                .forEach(eventRegistry::register);
    }
}

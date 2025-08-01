package com.github.mangila.webshop.app.config;

import com.github.mangila.webshop.shared.annotation.DomainObject;
import com.github.mangila.webshop.shared.model.Domain;
import com.github.mangila.webshop.shared.registry.DomainRegistry;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class DomainRegistryConfig {

    private static final Logger log = LoggerFactory.getLogger(DomainRegistryConfig.class);

    @Bean
    Map<Domain, String> domainMap() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    DomainRegistry domainRegistry() {
        var domainRegistry = new DomainRegistry(domainMap());
        registerDomains(domainRegistry);
        return domainRegistry;
    }

    void registerDomains(DomainRegistry domainRegistry) {
        Class<DomainObject> annotation = DomainObject.class;
        log.info("Scan {}", annotation.getName());
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forJavaClassPath())
                .setScanners(Scanners.TypesAnnotated));
        reflections.getTypesAnnotatedWith(annotation)
                .forEach(domainClazz -> {
                    log.info("Register domain: {}", domainClazz.getSimpleName().toUpperCase());
                    var domain = new Domain(domainClazz);
                    domainRegistry.register(domain, domain.value());
                });
    }

}

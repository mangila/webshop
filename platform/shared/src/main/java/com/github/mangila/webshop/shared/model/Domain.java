package com.github.mangila.webshop.shared.model;

import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.registry.DomainRegistry;

public record Domain(String value) {

    public Domain {
        Ensure.notBlank(value, String.class);
    }

    public Domain(Class<?> domain) {
        this(domain.getSimpleName().toUpperCase());
    }

    public Domain(Class<?> domain, DomainRegistry registry) {
        this(domain.getSimpleName().toUpperCase());
        registry.ensureIsRegistered(this);
    }

    public Domain(String domain, DomainRegistry registry) {
        this(domain);
        registry.ensureIsRegistered(this);
    }
}
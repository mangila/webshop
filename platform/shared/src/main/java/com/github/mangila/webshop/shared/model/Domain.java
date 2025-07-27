package com.github.mangila.webshop.shared.model;

import com.github.mangila.webshop.shared.registry.RegistryService;
import com.github.mangila.webshop.shared.Ensure;

public record Domain(String value) {

    public Domain {
        Ensure.notBlank(value, "Domain cannot be blank");
    }

    public Domain(Class<?> domain) {
        this(domain.getSimpleName().toUpperCase());
    }

    public Domain(Class<?> domain, RegistryService registry) {
        this(domain.getSimpleName().toUpperCase());
        registry.ensureIsRegistered(this);
    }

    public Domain(String domain, RegistryService registry) {
        this(domain);
        registry.ensureIsRegistered(this);
    }
}
package com.github.mangila.webshop.shared.application.registry;

import java.util.Objects;

public final class Domain {

    private final String value;

    private Domain(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static Domain from(Class<?> domain) {
        return new Domain(domain.getSimpleName().toUpperCase());
    }

    public static Domain from(String domain, RegistryService registry) {
        var d = new Domain(domain);
        registry.ensureIsRegistered(d);
        return d;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Domain domain = (Domain) o;
        return Objects.equals(value, domain.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}


package com.github.mangila.webshop.shared.application.registry;

import java.util.Objects;

public final class DomainKey {

    private final String value;

    private DomainKey(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static DomainKey create(Class<?> domain) {
        return new DomainKey(domain.getSimpleName().toUpperCase());
    }

    public static DomainKey from(String domain, RegistryService registry) {
        var key = new DomainKey(domain);
        registry.ensureDomainIsRegistered(key);
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomainKey domainKey = (DomainKey) o;
        return Objects.equals(value, domainKey.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}


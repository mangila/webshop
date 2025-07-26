package com.github.mangila.webshop.shared.registry.model;

import com.github.mangila.webshop.shared.registry.RegistryService;
import com.github.mangila.webshop.shared.util.Ensure;

import java.util.Objects;

public final class Domain {

    private final String value;

    private Domain(String value) {
        Ensure.notBlank(value, "Domain cannot be blank");
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static Domain createNew(Class<?> domain) {
        return new Domain(domain.getSimpleName().toUpperCase());
    }

    public static Domain from(Class<?> domain, RegistryService registry) {
        var d = createNew(domain);
        registry.ensureIsRegistered(d);
        return d;
    }

    public static Domain from(String domain, RegistryService registry) {
        var d = new Domain(domain);
        registry.ensureIsRegistered(d);
        return d;
    }

    @Override
    public String toString() {
        return "Domain{" +
               "value='" + value + '\'' +
               '}';
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


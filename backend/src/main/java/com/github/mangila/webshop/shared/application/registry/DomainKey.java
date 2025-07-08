package com.github.mangila.webshop.shared.application.registry;

public record DomainKey(String value) {

    public static DomainKey from(Class<?> domain) {
        return new DomainKey(domain.getSimpleName().toUpperCase());
    }

    public static DomainKey from(String domain) {
        return new DomainKey(domain);
    }
}


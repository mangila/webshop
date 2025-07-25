package com.github.mangila.webshop.shared.util;

import java.util.List;

public final class CacheName {
    public static final String LRU = "LRU";
    public static final String TTL = "TTL";
    public static final String EVENT_REGISTRY = "EVENT_REGISTRY";
    public static final String DOMAIN_REGISTRY = "DOMAIN_REGISTRY";

    public static final List<String> ALL = List.of(
            LRU,
            TTL,
            EVENT_REGISTRY,
            DOMAIN_REGISTRY);

    private CacheName() {
        throw new ApplicationException("Utility class");
    }
}


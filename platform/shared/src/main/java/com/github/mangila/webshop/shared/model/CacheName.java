package com.github.mangila.webshop.shared.model;

import com.github.mangila.webshop.shared.exception.ApplicationException;

import java.util.List;

public final class CacheName {
    public static final String LRU = "LRU";
    public static final String TTL = "TTL";

    public static final List<String> ALL = List.of(LRU, TTL);

    private CacheName() {
        throw new ApplicationException("Utility class");
    }
}


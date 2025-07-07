package com.github.mangila.webshop.shared.application.registry;

import java.util.List;

public interface DomainRegistry {

    boolean isRegistered(String key);

    void register(String key, String value);

    List<String> values();
}

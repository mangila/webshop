package com.github.mangila.webshop.backend.event.application;

import org.springframework.stereotype.Service;

@Service
public class DefaultEventTypeRegistry implements EventRegistry {

    @Override
    public boolean isRegistered(String key) {
        return false;
    }

    @Override
    public void register(String key, String value) {

    }
}

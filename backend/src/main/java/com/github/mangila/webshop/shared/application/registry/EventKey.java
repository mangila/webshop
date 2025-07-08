package com.github.mangila.webshop.shared.application.registry;

public record EventKey(String value) {

    public static EventKey from(Enum<?> event) {
        return new EventKey(event.name());
    }

    public static EventKey from(String event) {
        return new EventKey(event);
    }
}


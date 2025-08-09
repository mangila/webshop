package com.github.mangila.webshop.shared.model;

import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.registry.EventRegistry;

public record Event(String value) {

    public static final Event EMPTY = new Event("EMPTY");

    public Event {
        Ensure.notBlank(value, String.class);
    }

    public Event(Enum<?> event) {
        this(event.name());
    }

    public Event(Enum<?> event, EventRegistry registry) {
        this(event.name());
        registry.ensureIsRegistered(this);
    }

    public Event(String event, EventRegistry registry) {
        this(event);
        registry.ensureIsRegistered(this);
    }
}


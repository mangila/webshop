package com.github.mangila.webshop.shared.model;

import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.registry.EventRegistry;

public record Event(String value) {

    public Event {
        Ensure.notBlank(value, Event.class);
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


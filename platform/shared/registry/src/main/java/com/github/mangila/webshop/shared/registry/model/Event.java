package com.github.mangila.webshop.shared.registry.model;

import com.github.mangila.webshop.shared.registry.RegistryService;
import com.github.mangila.webshop.shared.util.Ensure;

public record Event(String value) {

    public Event {
        Ensure.notBlank(value, "Event cannot be blank");
    }

    public Event(Enum<?> event) {
        this(event.name());
    }

    public Event(Enum<?> event, RegistryService registry) {
        this(event.name());
        registry.ensureIsRegistered(this);
    }

    public Event(String domain, RegistryService registry) {
        this(domain);
        registry.ensureIsRegistered(this);
    }
}


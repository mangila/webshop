package com.github.mangila.webshop.shared.application.registry;

import java.util.Objects;

public final class EventKey {
    private final String value;

    private EventKey(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static EventKey create(Enum<?> event) {
        return new EventKey(event.name());
    }

    public static EventKey from(String event, RegistryService registry) {
        var key = new EventKey(event);
        registry.ensureEventIsRegistered(key);
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventKey eventKey = (EventKey) o;
        return Objects.equals(value, eventKey.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}


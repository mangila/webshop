package com.github.mangila.webshop.shared.registry.model;

import com.github.mangila.webshop.shared.registry.RegistryService;
import com.github.mangila.webshop.shared.util.Ensure;

import java.util.Objects;

public final class Event {

    private final String value;

    private Event(String value) {
        Ensure.notBlank(value, "Event cannot be blank");
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static Event from(Enum<?> event) {
        return new Event(event.name());
    }

    public static Event from(String event, RegistryService registry) {
        var e = new Event(event);
        registry.ensureIsRegistered(e);
        return e;
    }

    @Override
    public String toString() {
        return "Event{" +
               "value='" + value + '\'' +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(value, event.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}


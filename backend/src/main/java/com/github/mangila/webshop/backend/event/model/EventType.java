package com.github.mangila.webshop.backend.event.model;

public interface EventType {

    EventTopic topic();

    String type();

    default Class<? extends EventType> getEnumClass() {
        return this.getClass();
    }
}

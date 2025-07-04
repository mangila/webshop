package com.github.mangila.webshop.backend.outboxevent.domain.query;

public record OutboxEventFindByIdQuery(long id) {

    public static OutboxEventFindByIdQuery from(String id) {
        return new OutboxEventFindByIdQuery(Long.parseLong(id));
    }

}

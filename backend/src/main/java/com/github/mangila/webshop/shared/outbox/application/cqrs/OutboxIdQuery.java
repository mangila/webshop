package com.github.mangila.webshop.shared.outbox.application.cqrs;

public record OutboxIdQuery(long id) {

    public static OutboxIdQuery from(String id) {
        return new OutboxIdQuery(Long.parseLong(id));
    }

}

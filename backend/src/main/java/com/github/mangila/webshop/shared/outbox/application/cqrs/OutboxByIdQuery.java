package com.github.mangila.webshop.shared.outbox.application.cqrs;

public record OutboxByIdQuery(long id) {

    public static OutboxByIdQuery from(String id) {
        return new OutboxByIdQuery(Long.parseLong(id));
    }

}

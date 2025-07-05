package com.github.mangila.webshop.shared.outbox.application.cqrs;

public record OutboxFindByIdQuery(long id) {

    public static OutboxFindByIdQuery from(String id) {
        return new OutboxFindByIdQuery(Long.parseLong(id));
    }

}

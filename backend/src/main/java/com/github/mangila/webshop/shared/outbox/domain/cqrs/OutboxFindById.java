package com.github.mangila.webshop.shared.outbox.domain.cqrs;

public record OutboxFindById(long id) {

    public static OutboxFindById from(long id) {
        return new OutboxFindById(id);
    }
}

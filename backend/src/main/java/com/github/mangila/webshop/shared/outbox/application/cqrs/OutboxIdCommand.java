package com.github.mangila.webshop.shared.outbox.application.cqrs;

public record OutboxIdCommand(long id) {

    public static OutboxIdCommand from(String id) {
        return new OutboxIdCommand(Long.parseLong(id));
    }

}

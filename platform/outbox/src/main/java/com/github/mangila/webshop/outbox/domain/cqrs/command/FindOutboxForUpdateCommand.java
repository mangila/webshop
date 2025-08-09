package com.github.mangila.webshop.outbox.domain.cqrs.command;

import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;

public record FindOutboxForUpdateCommand(OutboxId id) {
}

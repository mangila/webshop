package com.github.mangila.webshop.outbox.domain.cqrs.command;

import com.github.mangila.webshop.outbox.domain.OutboxSequence;

public record UpdateOutboxSequenceCommand(OutboxSequence sequence) {
    public static UpdateOutboxSequenceCommand from(OutboxSequence newSequence) {
        return new UpdateOutboxSequenceCommand(newSequence);
    }
}

package com.github.mangila.webshop.outbox.domain.cqrs.command;

import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxUpdated;
import com.github.mangila.webshop.outbox.domain.types.OutboxStatusType;
import com.github.mangila.webshop.shared.Ensure;

public record UpdateOutboxStatusCommand(
        OutboxId id,
        OutboxStatusType outboxStatusType,
        OutboxUpdated outboxUpdated
) {
    public UpdateOutboxStatusCommand {
        Ensure.notNull(id, OutboxId.class);
        Ensure.notNull(outboxStatusType, OutboxStatusType.class);
        Ensure.notNull(outboxUpdated, OutboxUpdated.class);
    }

    public static UpdateOutboxStatusCommand processing(OutboxId id) {
        return new UpdateOutboxStatusCommand(
                id,
                OutboxStatusType.PROCESSING,
                OutboxUpdated.now()
        );
    }

    public static UpdateOutboxStatusCommand published(OutboxId id) {
        return new UpdateOutboxStatusCommand(
                id,
                OutboxStatusType.PUBLISHED,
                OutboxUpdated.now()
        );
    }

    public static UpdateOutboxStatusCommand failed(OutboxId id) {
        return new UpdateOutboxStatusCommand(
                id,
                OutboxStatusType.FAILED,
                OutboxUpdated.now()
        );
    }
}

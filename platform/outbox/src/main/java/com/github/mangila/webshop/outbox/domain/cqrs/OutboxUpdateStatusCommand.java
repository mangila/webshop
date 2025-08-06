package com.github.mangila.webshop.outbox.domain.cqrs;

import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxUpdated;
import com.github.mangila.webshop.outbox.domain.types.OutboxStatusType;
import com.github.mangila.webshop.shared.Ensure;

public record OutboxUpdateStatusCommand(
        OutboxId id,
        OutboxStatusType outboxStatusType,
        OutboxUpdated outboxUpdated
) {
    public OutboxUpdateStatusCommand {
        Ensure.notNull(id, OutboxId.class);
        Ensure.notNull(outboxStatusType, OutboxStatusType.class);
        Ensure.notNull(outboxUpdated, OutboxUpdated.class);
    }

    public static OutboxUpdateStatusCommand published(OutboxId id) {
        return new OutboxUpdateStatusCommand(
                id,
                OutboxStatusType.PUBLISHED,
                OutboxUpdated.now()
        );
    }

    public static OutboxUpdateStatusCommand failed(OutboxId id) {
        return new OutboxUpdateStatusCommand(
                id,
                OutboxStatusType.FAILED,
                OutboxUpdated.now()
        );
    }
}

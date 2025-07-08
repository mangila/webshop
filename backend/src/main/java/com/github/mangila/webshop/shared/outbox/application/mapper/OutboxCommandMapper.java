package com.github.mangila.webshop.shared.outbox.application.mapper;

import com.github.mangila.webshop.shared.outbox.application.cqrs.OutboxIdCommand;
import com.github.mangila.webshop.shared.outbox.application.cqrs.OutboxInsertCommand;
import com.github.mangila.webshop.shared.outbox.domain.cqrs.OutboxInsert;
import com.github.mangila.webshop.shared.outbox.domain.primitive.OutboxId;
import org.springframework.stereotype.Component;

@Component
public class OutboxCommandMapper {

    public OutboxInsert toDomain(OutboxInsertCommand command) {
        return OutboxInsert.from(
                command.domain().value(),
                command.event().value(),
                command.aggregateId(),
                command.payload()
        );
    }

    public OutboxId toDomain(OutboxIdCommand command) {
        return new OutboxId(command.id());
    }
}

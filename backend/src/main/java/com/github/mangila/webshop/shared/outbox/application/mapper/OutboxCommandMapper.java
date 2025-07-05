package com.github.mangila.webshop.shared.outbox.application.mapper;

import com.github.mangila.webshop.shared.outbox.application.cqrs.OutboxInsertCommand;
import com.github.mangila.webshop.shared.outbox.domain.cqrs.OutboxInsert;
import org.springframework.stereotype.Component;

@Component
public class OutboxCommandMapper {

    public OutboxInsert toDomain(OutboxInsertCommand command) {
        return OutboxInsert.from(
                command.topic(),
                command.event(),
                command.aggregateId(),
                command.payload()
        );
    }
}

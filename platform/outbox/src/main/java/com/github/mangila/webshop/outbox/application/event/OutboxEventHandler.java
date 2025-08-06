package com.github.mangila.webshop.outbox.application.event;

import com.github.mangila.webshop.outbox.application.service.OutboxCommandService;
import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxSequence;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxInsertCommand;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxAggregateId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxPayload;
import com.github.mangila.webshop.shared.model.OutboxEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OutboxEventHandler {

    private final OutboxCommandService commandService;
    private final OutboxEventMapper eventMapper = new OutboxEventMapper();

    public OutboxEventHandler(OutboxCommandService commandService) {
        this.commandService = commandService;
    }

    public Outbox handle(OutboxEvent event) {
        var aggregateId = new OutboxAggregateId(event.aggregateId());
        OutboxSequence newSequence = commandService.findByAggregateIdAndIncrementForUpdate(aggregateId);
        OutboxInsertCommand command = eventMapper.toCommand(event, newSequence);
        Outbox outbox = commandService.insert(command);
        commandService.updateSequence(newSequence);
        return outbox;
    }

    private final static class OutboxEventMapper {
        public OutboxInsertCommand toCommand(OutboxEvent event, OutboxSequence sequence) {
            UUID aggregateId = event.aggregateId();
            return new OutboxInsertCommand(
                    event.domain(),
                    event.event(),
                    new OutboxAggregateId(aggregateId),
                    new OutboxPayload(event.payload()),
                    sequence
            );
        }
    }
}

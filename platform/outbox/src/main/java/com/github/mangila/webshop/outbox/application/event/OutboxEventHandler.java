package com.github.mangila.webshop.outbox.application.event;

import com.github.mangila.webshop.outbox.application.action.command.CreateOutboxCommandAction;
import com.github.mangila.webshop.outbox.application.action.command.IncrementOutboxSequenceCommandAction;
import com.github.mangila.webshop.outbox.application.action.command.UpdateOutboxSequenceCommandAction;
import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxSequence;
import com.github.mangila.webshop.outbox.domain.cqrs.command.CreateOutboxCommand;
import com.github.mangila.webshop.outbox.domain.cqrs.command.IncrementOutboxSequenceCommand;
import com.github.mangila.webshop.outbox.domain.cqrs.command.UpdateOutboxSequenceCommand;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxAggregateId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxPayload;
import com.github.mangila.webshop.shared.model.OutboxEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OutboxEventHandler {

    private final UpdateOutboxSequenceCommandAction updateOutboxSequenceCommandAction;
    private final IncrementOutboxSequenceCommandAction incrementOutboxSequenceCommandAction;
    private final CreateOutboxCommandAction createOutboxCommandAction;
    private final OutboxEventMapper eventMapper = new OutboxEventMapper();

    public OutboxEventHandler(UpdateOutboxSequenceCommandAction updateOutboxSequenceCommandAction, IncrementOutboxSequenceCommandAction incrementOutboxSequenceCommandAction, CreateOutboxCommandAction createOutboxCommandAction) {
        this.updateOutboxSequenceCommandAction = updateOutboxSequenceCommandAction;
        this.incrementOutboxSequenceCommandAction = incrementOutboxSequenceCommandAction;
        this.createOutboxCommandAction = createOutboxCommandAction;
    }


    public Outbox handle(OutboxEvent event) {
        var aggregateId = new OutboxAggregateId(event.aggregateId());
        OutboxSequence newSequence = incrementOutboxSequenceCommandAction.execute(IncrementOutboxSequenceCommand.from((aggregateId)));
        CreateOutboxCommand command = eventMapper.toCommand(event, newSequence);
        Outbox outbox = createOutboxCommandAction.execute(command);
        updateOutboxSequenceCommandAction.execute(UpdateOutboxSequenceCommand.from(newSequence));
        return outbox;
    }

    private final static class OutboxEventMapper {
        public CreateOutboxCommand toCommand(OutboxEvent event, OutboxSequence sequence) {
            UUID aggregateId = event.aggregateId();
            return new CreateOutboxCommand(
                    event.domain(),
                    event.event(),
                    new OutboxAggregateId(aggregateId),
                    new OutboxPayload(event.payload()),
                    sequence
            );
        }
    }
}

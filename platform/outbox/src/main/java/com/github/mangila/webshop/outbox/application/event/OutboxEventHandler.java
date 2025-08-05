package com.github.mangila.webshop.outbox.application.event;

import com.github.mangila.webshop.outbox.application.service.OutboxCommandService;
import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxSequence;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxInsertCommand;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxAggregateId;
import com.github.mangila.webshop.shared.model.OutboxEvent;
import org.springframework.stereotype.Component;

@Component
public class OutboxEventHandler {

    private final OutboxCommandService commandService;
    private final OutboxEventMapper eventMapper;

    public OutboxEventHandler(OutboxCommandService commandService,
                              OutboxEventMapper eventMapper) {
        this.commandService = commandService;
        this.eventMapper = eventMapper;
    }

    public Outbox handle(OutboxEvent event) {
        var aggregateId = new OutboxAggregateId(event.aggregateId());
        OutboxSequence newSequence = commandService.findByAggregateIdAndIncrementForUpdate(aggregateId);
        OutboxInsertCommand command = eventMapper.toCommand(event, newSequence);
        Outbox outbox = commandService.insert(command);
        commandService.updateSequence(newSequence);
        return outbox;
    }

}

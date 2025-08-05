package com.github.mangila.webshop.inbox.application.event;

import com.github.mangila.webshop.inbox.application.InboxCommandService;
import com.github.mangila.webshop.inbox.domain.cqrs.InboxInsertCommand;
import com.github.mangila.webshop.inbox.domain.primitive.InboxAggregateId;
import com.github.mangila.webshop.inbox.domain.primitive.InboxPayload;
import com.github.mangila.webshop.inbox.domain.primitive.InboxSequence;
import com.github.mangila.webshop.shared.model.InboxEvent;
import org.springframework.stereotype.Service;

@Service
public class InboxEventHandler {

    private final InboxCommandService commandService;
    private final InboxEventMapper mapper = new InboxEventMapper();

    public InboxEventHandler(InboxCommandService commandService) {
        this.commandService = commandService;
    }

    public void handle(InboxEvent event) {
        var command = mapper.toCommand(event);
        commandService.insert(command);
    }

    private static final class InboxEventMapper {
        private InboxInsertCommand toCommand(InboxEvent event) {
            return new InboxInsertCommand(
                    new InboxAggregateId(event.aggregateId()),
                    event.domain(),
                    event.event(),
                    new InboxPayload(event.payload()),
                    new InboxSequence(event.sequence()),
                    event.source()
            );
        }
    }
}

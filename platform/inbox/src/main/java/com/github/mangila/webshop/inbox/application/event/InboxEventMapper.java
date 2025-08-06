package com.github.mangila.webshop.inbox.application.event;

import com.github.mangila.webshop.inbox.domain.cqrs.InboxInsertCommand;
import com.github.mangila.webshop.inbox.domain.primitive.InboxAggregateId;
import com.github.mangila.webshop.inbox.domain.primitive.InboxPayload;
import com.github.mangila.webshop.inbox.domain.primitive.InboxSequence;
import com.github.mangila.webshop.shared.model.InboxEvent;
import org.springframework.stereotype.Component;

@Component
public class InboxEventMapper {
    public InboxInsertCommand toCommand(InboxEvent event) {
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

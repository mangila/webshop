package com.github.mangila.webshop.shared;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.shared.annotation.RegisteredDomain;
import com.github.mangila.webshop.shared.annotation.RegisteredEvent;
import com.github.mangila.webshop.shared.identity.application.validation.GeneratedIdentity;
import com.github.mangila.webshop.shared.model.Domain;
import com.github.mangila.webshop.shared.model.Event;
import com.github.mangila.webshop.shared.model.OutboxEvent;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Component
@Validated
public class OutboxEventMapper {
    public OutboxEvent toEvent(@RegisteredDomain Domain domain,
                               @RegisteredEvent Event event,
                               @GeneratedIdentity UUID aggregateId,
                               @NotNull ObjectNode payload) {
        return new OutboxEvent(
                domain,
                event,
                aggregateId,
                payload
        );
    }
}

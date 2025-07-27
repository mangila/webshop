package com.github.mangila.webshop.outbox.application;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.identity.application.validation.GeneratedIdentity;
import com.github.mangila.webshop.shared.annotation.RegisteredDomain;
import com.github.mangila.webshop.shared.annotation.RegisteredEvent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record OutboxDto(
        @Min(1)
        long id,
        @RegisteredDomain
        String domain,
        @RegisteredEvent
        String event,
        @NotNull
        @GeneratedIdentity
        UUID aggregateId,
        @NotNull
        ObjectNode payload,
        boolean published,
        @Min(1)
        int sequence,
        @NotNull
        Instant created
) {
}


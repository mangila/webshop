package com.github.mangila.webshop.outbox.application.graphql.input;

import com.github.mangila.webshop.shared.identity.application.validation.GeneratedIdentity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OutboxReplayInput(
        @NotNull
        @GeneratedIdentity
        UUID aggregateId,
        @Min(1)
        int sequence,
        @Min(1)
        @Max(500)
        int limit
) {
}
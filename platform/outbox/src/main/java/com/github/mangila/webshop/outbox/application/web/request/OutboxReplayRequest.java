package com.github.mangila.webshop.outbox.application.web.request;

import com.github.mangila.webshop.identity.application.validation.DomainId;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OutboxReplayRequest(
        @NotNull
        @DomainId
        UUID aggregateId,
        @Min(1)
        int sequence,
        @Min(1)
        @Max(500)
        int limit
) {
}
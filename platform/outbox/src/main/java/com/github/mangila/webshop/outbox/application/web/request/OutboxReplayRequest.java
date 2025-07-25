package com.github.mangila.webshop.outbox.application.web.request;

import com.github.mangila.webshop.shared.validation.DomainId;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.UUID;

public record OutboxReplayRequest(
        @DomainId
        UUID aggregateId,
        @Min(1)
        int sequence,
        @Min(1)
        @Max(500)
        int limit
) {
}

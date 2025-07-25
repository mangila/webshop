package com.github.mangila.webshop.outbox.application.web.request;

import com.github.mangila.webshop.shared.validation.DomainId;

import java.util.UUID;

public record OutboxReplayRequest(
        @DomainId UUID aggregateId,
        int sequence,
        int limit
) {
}

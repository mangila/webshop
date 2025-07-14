package com.github.mangila.webshop.shared.outbox.application.dto;

import java.util.UUID;

public record OutboxMessageDto(long id,
                               UUID aggregateId,
                               String domain,
                               String event) {
}

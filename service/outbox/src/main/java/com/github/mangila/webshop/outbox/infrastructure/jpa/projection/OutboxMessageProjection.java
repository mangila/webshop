package com.github.mangila.webshop.outbox.infrastructure.jpa.projection;

import java.util.UUID;

public record OutboxMessageProjection(long id,
                                      UUID aggregateId,
                                      String domain,
                                      String event) {
}

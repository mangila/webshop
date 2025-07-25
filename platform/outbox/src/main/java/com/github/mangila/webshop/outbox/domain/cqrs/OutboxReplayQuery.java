package com.github.mangila.webshop.outbox.domain.cqrs;

import com.github.mangila.webshop.outbox.domain.OutboxSequence;

public record OutboxReplayQuery(OutboxSequence sequence, int limit) {
}

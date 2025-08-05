package com.github.mangila.webshop.inbox.domain.primitive;

import com.github.mangila.webshop.shared.Ensure;

import java.util.UUID;

public record InboxAggregateId(UUID value) {
    public InboxAggregateId {
        Ensure.notNull(value, UUID.class);
    }
}

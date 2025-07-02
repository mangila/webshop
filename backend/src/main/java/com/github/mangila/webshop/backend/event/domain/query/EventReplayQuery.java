package com.github.mangila.webshop.backend.event.domain.query;

import com.github.mangila.webshop.backend.common.model.ApplicationUuid;

public record EventReplayQuery(
        String topic,
        ApplicationUuid aggregateId,
        long offset,
        int limit
) {
}

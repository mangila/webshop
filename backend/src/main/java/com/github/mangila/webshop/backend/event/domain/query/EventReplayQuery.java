package com.github.mangila.webshop.backend.event.domain.query;

import com.github.mangila.webshop.backend.common.model.ApplicationUuid;
import com.github.mangila.webshop.backend.event.domain.model.EventTopic;

public record EventReplayQuery(
        EventTopic topic,
        ApplicationUuid aggregateId,
        long offset,
        int limit
) {
}

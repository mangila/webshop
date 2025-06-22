package com.github.mangila.webshop.backend.event.query.model;

import com.github.mangila.webshop.backend.event.model.EventTopic;

public record EventReplayQuery(
        EventTopic topic,
        String aggregateId,
        long offset,
        int limit
) {
}

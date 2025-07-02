package com.github.mangila.webshop.backend.event.domain.query;

public record EventFindByTopicAndTypeAndOffsetQuery(
        String topic,
        String type,
        long offset,
        int limit
) {
}

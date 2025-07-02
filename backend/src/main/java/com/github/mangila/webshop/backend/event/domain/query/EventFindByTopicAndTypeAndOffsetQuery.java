package com.github.mangila.webshop.backend.event.domain.query;

public record EventFindByTopicAndTypeAndOffsetQuery(
        String topic,
        String type,
        long offset,
        int limit
) {
    public static EventFindByTopicAndTypeAndOffsetQuery from(String topic,
                                                             String type,
                                                             Long latestOffset,
                                                             int limit) {
        return new EventFindByTopicAndTypeAndOffsetQuery(topic, type, latestOffset, limit);
    }
}

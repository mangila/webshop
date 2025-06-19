package com.github.mangila.webshop.event.query.model;

public record EventQueryReplay(
        String topic,
        String aggregateId,
        Long offset,
        Integer limit
) {
    private static final int DEFAULT_OFFSET = 1;
    private static final int DEFAULT_LIMIT = 100;

    @Override
    public Long offset() {
        return offset == null ? DEFAULT_OFFSET : offset;
    }

    @Override
    public Integer limit() {
        return limit == null ? DEFAULT_LIMIT : limit;
    }
}

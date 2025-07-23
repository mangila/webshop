package com.github.mangila.webshop.outbox.domain.primitive;

public record OutboxPublished(boolean value) {

    private static final OutboxPublished PUBLISHED = published();
    private static final OutboxPublished NOT_PUBLISHED = notPublished();

    public static OutboxPublished published() {
        return PUBLISHED;
    }

    public static OutboxPublished notPublished() {
        return NOT_PUBLISHED;
    }

}

package com.github.mangila.webshop.outbox.domain.primitive;

public record OutboxPublished(boolean value) {

    private static final OutboxPublished PUBLISHED = new OutboxPublished(true);
    private static final OutboxPublished NOT_PUBLISHED = new OutboxPublished(false);

    public static OutboxPublished published() {
        return PUBLISHED;
    }

    public static OutboxPublished notPublished() {
        return NOT_PUBLISHED;
    }

}

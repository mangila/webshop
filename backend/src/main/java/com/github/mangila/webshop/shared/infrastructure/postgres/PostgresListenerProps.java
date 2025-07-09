package com.github.mangila.webshop.shared.infrastructure.postgres;

public record PostgresListenerProps(
        String tableName,
        String channelName,
        String functionName,
        String triggerName,
        int pollTimeoutMillis) {
}

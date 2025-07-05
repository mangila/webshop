package com.github.mangila.webshop.shared.application.message.postgres;

public record PostgresListenerProps(
        String tableName,
        String channelName,
        String functionName,
        String triggerName) {
}

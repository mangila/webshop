package com.github.mangila.webshop.backend.common.props;

public record PostgresListenerProps(
        String tableName,
        String channelName,
        String functionName,
        String triggerName) {
}

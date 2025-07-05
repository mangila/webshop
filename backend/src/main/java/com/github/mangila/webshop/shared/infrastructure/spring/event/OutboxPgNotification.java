package com.github.mangila.webshop.shared.infrastructure.spring.event;

import org.postgresql.PGNotification;

public record OutboxPgNotification(PGNotification notification) {
}

package com.github.mangila.webshop.shared.application.message.springevent;

import org.postgresql.PGNotification;

public record OutboxPgNotification(PGNotification notification) {
}

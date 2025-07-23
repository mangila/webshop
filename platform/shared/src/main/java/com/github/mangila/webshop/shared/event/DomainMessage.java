package com.github.mangila.webshop.shared.event;

import com.github.mangila.webshop.shared.model.Domain;
import com.github.mangila.webshop.shared.model.Event;

import java.util.UUID;

public record DomainMessage(
        long id,
        UUID aggregateId,
        Domain domain,
        Event event
) {
}

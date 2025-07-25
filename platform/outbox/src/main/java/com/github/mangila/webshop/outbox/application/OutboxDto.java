package com.github.mangila.webshop.outbox.application;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.outbox.domain.OutboxSequence;
import com.github.mangila.webshop.outbox.domain.primitive.*;
import com.github.mangila.webshop.shared.registry.model.Domain;
import com.github.mangila.webshop.shared.registry.model.Event;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.util.UUID;

public record OutboxDto(
        Long id,
        String domain,
        String event,
        UUID aggregateId,
        ObjectNode payload,
        boolean published,
        int sequence,
        Instant created
) {
}


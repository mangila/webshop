package com.github.mangila.webshop.outbox.domain.primitive;


import com.github.mangila.webshop.shared.Ensure;

public record OutboxId(long value) {
    public OutboxId {
        Ensure.min(1, value, "OutboxId must be greater than 0");
    }
}

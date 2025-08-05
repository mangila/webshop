package com.github.mangila.webshop.inbox.domain.primitive;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.shared.Ensure;

public record InboxPayload(ObjectNode value) {
    public InboxPayload {
        Ensure.notNull(value, ObjectNode.class);
    }
}

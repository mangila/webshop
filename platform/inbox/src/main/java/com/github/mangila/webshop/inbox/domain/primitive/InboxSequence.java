package com.github.mangila.webshop.inbox.domain.primitive;

import com.github.mangila.webshop.shared.Ensure;

public record InboxSequence(int value) {
    public InboxSequence {
        Ensure.min(1, value);
    }
}

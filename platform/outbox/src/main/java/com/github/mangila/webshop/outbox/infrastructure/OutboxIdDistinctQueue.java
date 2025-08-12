package com.github.mangila.webshop.outbox.infrastructure;

import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.shared.DistinctQueue;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

@Component
public class OutboxIdDistinctQueue {
    private final DistinctQueue<OutboxId> queue;
    private final DistinctQueue<OutboxId> dlq;

    public OutboxIdDistinctQueue() {
        this.queue = new DistinctQueue<>();
        this.dlq = new DistinctQueue<>();
    }

    public void add(OutboxId id) {
        queue.add(id);
    }

    public void addDlq(OutboxId id) {
        dlq.add(id);
    }

    public @Nullable OutboxId poll() {
        return queue.poll();
    }

    public @Nullable OutboxId pollDlq() {
        return dlq.poll();
    }

}

package com.github.mangila.webshop.shared;

import com.github.mangila.webshop.shared.model.Domain;
import org.jspecify.annotations.Nullable;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class InternalQueue<T> {

    private final Domain domain;
    private final Queue<T> queue;
    private final Queue<T> dlq;

    public InternalQueue(Domain domain) {
        this.domain = domain;
        this.queue = new ConcurrentLinkedQueue<>();
        this.dlq = new ConcurrentLinkedQueue<>();
    }

    public Domain domain() {
        return domain;
    }

    public void add(T type) {
        queue.add(type);
    }

    public @Nullable T poll() {
        return queue.poll();
    }

    public void addDlq(T type) {
        dlq.add(type);
    }

    public @Nullable T pollDlq() {
        return dlq.poll();
    }

}

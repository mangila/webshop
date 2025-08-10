package com.github.mangila.webshop.shared;

import com.github.mangila.webshop.shared.model.Domain;
import org.jspecify.annotations.Nullable;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class InternalQueue<T> {

    private final Domain domain;
    private final Queue<T> queue;
    private final Set<T> inQueue;
    private final Queue<T> dlq;
    private final Set<T> inDlq;

    public InternalQueue(Domain domain) {
        Ensure.notNull(domain, Domain.class);
        this.domain = domain;
        this.queue = new ConcurrentLinkedQueue<>();
        this.inQueue = ConcurrentHashMap.newKeySet();
        this.dlq = new ConcurrentLinkedQueue<>();
        this.inDlq = ConcurrentHashMap.newKeySet();
    }

    public Domain domain() {
        return domain;
    }

    public void add(T type) {
        if (inQueue.add(type)) {
            queue.add(type);
        }
    }

    public @Nullable T poll() {
        T t = queue.poll();
        if (t != null) {
            inQueue.remove(t);
        }
        return t;
    }

    public void addDlq(T type) {
        if (inDlq.add(type)) {
            dlq.add(type);
        }
    }

    public @Nullable T pollDlq() {
        T t = dlq.poll();
        if (t != null) {
            inDlq.remove(t);
        }
        return t;
    }

}

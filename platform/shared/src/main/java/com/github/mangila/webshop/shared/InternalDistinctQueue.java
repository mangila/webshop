package com.github.mangila.webshop.shared;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * InternalDistinctQueue is a thread-safe queue implementation that ensures distinct elements are maintained within
 * two separate queues: a primary queue and a dead-letter queue (DLQ). This class aims to prevent duplicate entries
 * in the queues while allowing elements to be added or retrieved safely.
 *
 * @param <T> the type of elements maintained by this queue
 *            <p>
 *            Responsibilities:
 *            1. Facilitate the addition of distinct elements to a primary queue.
 *            2. Fetch and remove elements from the primary queue.
 *            3. Manage a separate dead-letter queue for processing unsuccessful or failed elements.
 *            4. Prevent duplication of elements across both queues during addition.
 *            <p>
 *            Key Features:
 *            - Thread-safe queue and set implementations for concurrent operations.
 *            - Avoids duplicate entries by tracking enqueued elements using sets.
 */
@Component
public class InternalDistinctQueue<T> {
    private final Queue<T> queue;
    private final Set<T> inQueue;
    private final Queue<T> dlq;
    private final Set<T> inDlq;

    public InternalDistinctQueue() {
        this.queue = new ConcurrentLinkedQueue<>();
        this.inQueue = ConcurrentHashMap.newKeySet();
        this.dlq = new ConcurrentLinkedQueue<>();
        this.inDlq = ConcurrentHashMap.newKeySet();
    }

    public void add(T type) {
        Ensure.notNull(type, type.getClass());
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
        Ensure.notNull(type, type.getClass());
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

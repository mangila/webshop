package com.github.mangila.webshop.shared;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A thread-safe queue implementation that ensures only distinct elements
 * are added. This class combines the properties of a queue with a set to
 * prevent duplicate entries in the queue.
 *
 * @param <T> The type of elements stored in the queue.
 */
public class DistinctQueue<T> {
    private final Queue<T> queue;
    private final Set<T> inQueue;

    public DistinctQueue() {
        this.queue = new ConcurrentLinkedQueue<>();
        this.inQueue = ConcurrentHashMap.newKeySet();
    }

    public void fill(List<T> types) {
        types.forEach(this::add);
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
}

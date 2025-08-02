package com.github.mangila.webshop.outbox.infrastructure.message.task;

import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OutboxTaskRunner {
    private final Map<OutboxTaskKey, OutboxTask> keyToTask;
    private final Map<String, OutboxTaskKey> nameToKey;

    public OutboxTaskRunner(Map<OutboxTaskKey, OutboxTask> keyToTask,
                            Map<String, OutboxTaskKey> nameToKey) {
        this.keyToTask = keyToTask;
        this.nameToKey = nameToKey;
    }

    public OutboxTaskKey findKeyOrThrow(String value) {
        Ensure.notNull(value, String.class);
        OutboxTaskKey key = nameToKey.get(value);
        Ensure.notNull(key, () -> new ResourceNotFoundException(
                OutboxTaskKey.class,
                value
        ));
        return key;
    }

    public void runTask(OutboxTaskKey key) {
        Ensure.notNull(key, OutboxTaskKey.class);
        OutboxTask task = keyToTask.get(key);
        Ensure.notNull(task, () -> new ResourceNotFoundException(
                OutboxTaskKey.class,
                key
        ));
        task.execute();
    }
}

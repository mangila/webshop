package com.github.mangila.webshop.outbox.infrastructure.scheduler.task;

import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.SimpleTask;
import com.github.mangila.webshop.shared.SimpleTaskRunner;
import com.github.mangila.webshop.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OutboxTaskRunner implements SimpleTaskRunner<OutboxTaskKey> {
    private final Map<OutboxTaskKey, SimpleTask<OutboxTaskKey>> keyToOutboxTask;

    public OutboxTaskRunner(Map<OutboxTaskKey, SimpleTask<OutboxTaskKey>> keyToOutboxTask) {
        this.keyToOutboxTask = keyToOutboxTask;
    }

    @Override
    public void execute(OutboxTaskKey key) {
        Ensure.notNull(key, OutboxTaskKey.class);
        var task = keyToOutboxTask.get(key);
        Ensure.notNull(task, () -> new ResourceNotFoundException(
                OutboxTaskKey.class,
                key
        ));
        task.execute();
    }
}

package com.github.mangila.webshop.outbox.infrastructure.scheduler.job;

import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.SimpleTask;
import com.github.mangila.webshop.shared.SimpleTaskRunner;
import com.github.mangila.webshop.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OutboxJobRunner implements SimpleTaskRunner<OutboxJobKey> {

    private final Map<OutboxJobKey, SimpleTask<OutboxJobKey>> outboxJobKeyToJob;

    public OutboxJobRunner(Map<OutboxJobKey, SimpleTask<OutboxJobKey>> outboxJobKeyToJob) {
        this.outboxJobKeyToJob = outboxJobKeyToJob;
    }

    @Override
    public void execute(OutboxJobKey key) {
        Ensure.notNull(key, OutboxJobKey.class);
        var task = outboxJobKeyToJob.get(key);
        Ensure.notNull(task, () -> new ResourceNotFoundException(
                OutboxJobKey.class,
                key
        ));
        task.execute();
    }
}

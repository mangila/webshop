package com.github.mangila.webshop.outbox.infrastructure.scheduler.job;

import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.SimpleJob;
import com.github.mangila.webshop.shared.SimpleJobRunner;
import com.github.mangila.webshop.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OutboxJobRunner implements SimpleJobRunner<OutboxJobKey> {

    private final Map<OutboxJobKey, SimpleJob<OutboxJobKey>> outboxJobKeyToJob;

    public OutboxJobRunner(Map<OutboxJobKey, SimpleJob<OutboxJobKey>> outboxJobKeyToJob) {
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

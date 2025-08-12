package com.github.mangila.webshop.outbox.infrastructure.scheduler;

import com.github.mangila.webshop.outbox.infrastructure.scheduler.job.OutboxJobKey;
import com.github.mangila.webshop.outbox.infrastructure.scheduler.job.OutboxJobRunner;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "app.outbox.scheduler.enabled", havingValue = "true")
public class OutboxScheduler {

    private final OutboxJobRunner outboxJobRunner;

    public OutboxScheduler(OutboxJobRunner outboxJobRunner) {
        this.outboxJobRunner = outboxJobRunner;
    }

    @PostConstruct
    void init() {
        deletePublished();
        fillQueues();
    }

    @Scheduled(fixedRateString = "${app.outbox.scheduler.delete-published.fixed-rate}")
    public void deletePublished() {
        outboxJobRunner.execute(new OutboxJobKey("DELETE_PUBLISHED"));
    }

    @Scheduled(fixedRateString = "${app.outbox.scheduler.fill-queues.fixed-rate}")
    public void fillQueues() {
        outboxJobRunner.execute(new OutboxJobKey("FILL_EVENT_QUEUE"));
    }
}

package com.github.mangila.webshop.outbox.infrastructure.scheduler;

import com.github.mangila.webshop.outbox.infrastructure.scheduler.job.DeletePublishedOutboxJob;
import com.github.mangila.webshop.outbox.infrastructure.scheduler.job.FillEventQueueOutboxJob;
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
        fillEventQueue();
    }

    @Scheduled(fixedRateString = "${app.outbox.scheduler.delete-published.fixed-rate}")
    void deletePublished() {
        outboxJobRunner.execute(DeletePublishedOutboxJob.KEY);
    }

    @Scheduled(fixedRateString = "${app.outbox.scheduler.fill-queues.fixed-rate}")
    void fillEventQueue() {
        outboxJobRunner.execute(FillEventQueueOutboxJob.KEY);
    }
}

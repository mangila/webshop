package com.github.mangila.webshop.outbox.config;

import com.github.mangila.webshop.outbox.application.action.command.DeleteOutboxCommandAction;
import com.github.mangila.webshop.outbox.application.action.query.FindAllOutboxIdsByStatusQueryAction;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.infrastructure.scheduler.job.DeletePublishedOutboxJob;
import com.github.mangila.webshop.outbox.infrastructure.scheduler.job.FillEventQueueOutboxJob;
import com.github.mangila.webshop.outbox.infrastructure.scheduler.job.OutboxJobKey;
import com.github.mangila.webshop.shared.DistinctQueue;
import com.github.mangila.webshop.shared.SimpleTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class OutboxJobConfig {

    private static final Logger log = LoggerFactory.getLogger(OutboxJobConfig.class);

    @Bean
    Map<OutboxJobKey, SimpleTask<OutboxJobKey>> outboxJobKeyToJob(
            FindAllOutboxIdsByStatusQueryAction findAllOutboxIdsByStatusQueryAction,
            DeleteOutboxCommandAction deleteOutboxCommandAction,
            DistinctQueue<OutboxId> eventQueue
    ) {
        return Map.ofEntries(
                addJob(new DeletePublishedOutboxJob(findAllOutboxIdsByStatusQueryAction, deleteOutboxCommandAction)),
                addJob(new FillEventQueueOutboxJob(findAllOutboxIdsByStatusQueryAction, eventQueue))
        );
    }

    Map.Entry<OutboxJobKey, SimpleTask<OutboxJobKey>> addJob(SimpleTask<OutboxJobKey> job) {
        log.info("Add Job: {}", job.key());
        return Map.entry(job.key(), job);
    }
}

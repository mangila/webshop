package com.github.mangila.webshop.outbox.config;

import com.github.mangila.webshop.outbox.application.action.command.DeleteOutboxCommandAction;
import com.github.mangila.webshop.outbox.application.action.query.FindAllOutboxIdsByStatusQueryAction;
import com.github.mangila.webshop.outbox.infrastructure.OutboxIdDistinctQueue;
import com.github.mangila.webshop.outbox.infrastructure.scheduler.job.DeletePublishedOutboxJob;
import com.github.mangila.webshop.outbox.infrastructure.scheduler.job.FillOutboxIdDistinctQueueOutboxJob;
import com.github.mangila.webshop.outbox.infrastructure.scheduler.job.OutboxJobKey;
import com.github.mangila.webshop.shared.SimpleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class OutboxJobConfig {

    private static final Logger log = LoggerFactory.getLogger(OutboxJobConfig.class);

    @Bean
    Map<OutboxJobKey, SimpleJob<OutboxJobKey>> outboxJobKeyToJob(
            FindAllOutboxIdsByStatusQueryAction findAllOutboxIdsByStatusQueryAction,
            DeleteOutboxCommandAction deleteOutboxCommandAction,
            OutboxIdDistinctQueue outboxIdDistinctQueue
    ) {
        return Map.ofEntries(
                addJob(new DeletePublishedOutboxJob(findAllOutboxIdsByStatusQueryAction, deleteOutboxCommandAction)),
                addJob(new FillOutboxIdDistinctQueueOutboxJob(findAllOutboxIdsByStatusQueryAction, outboxIdDistinctQueue))
        );
    }

    Map.Entry<OutboxJobKey, SimpleJob<OutboxJobKey>> addJob(SimpleJob<OutboxJobKey> job) {
        log.info("Add Job: {}", job.key());
        return Map.entry(job.key(), job);
    }
}

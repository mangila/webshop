package com.github.mangila.webshop.outbox.config;

import com.github.mangila.webshop.outbox.application.action.command.UpdateOutboxStatusCommandAction;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.infrastructure.message.MessageProcessor;
import com.github.mangila.webshop.outbox.infrastructure.scheduler.task.OutboxTaskKey;
import com.github.mangila.webshop.outbox.infrastructure.scheduler.task.ProcessDlqOutboxTask;
import com.github.mangila.webshop.outbox.infrastructure.scheduler.task.ProcessQueueOutboxTask;
import com.github.mangila.webshop.shared.InternalQueue;
import com.github.mangila.webshop.shared.SimpleTask;
import com.github.mangila.webshop.shared.model.Domain;
import com.github.mangila.webshop.shared.registry.DomainRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class OutboxTaskConfig {

    private static final Logger log = LoggerFactory.getLogger(OutboxTaskConfig.class);

    @Bean
    Map<Domain, InternalQueue<OutboxId>> domainToOutboxIdQueue(DomainRegistry domainRegistry) {
        return domainRegistry.keys()
                .stream()
                .peek(domain -> log.info("Create OutboxQueue for domain: {}", domain))
                .collect(Collectors.toMap(Function.identity(), InternalQueue::new));
    }

    @Bean
    Map<OutboxTaskKey, SimpleTask<OutboxTaskKey>> keyToOutboxTask(
            Map<Domain, InternalQueue<OutboxId>> domainToOutboxIdQueue,
            MessageProcessor messageProcessor,
            UpdateOutboxStatusCommandAction updateOutboxStatusCommandAction
    ) {
        var map = new ConcurrentHashMap<OutboxTaskKey, SimpleTask<OutboxTaskKey>>();
        domainToOutboxIdQueue.values().forEach(queue -> {
            map.putAll(Map.ofEntries(
                    addTask(new ProcessQueueOutboxTask(messageProcessor, queue)),
                    addTask(new ProcessDlqOutboxTask(updateOutboxStatusCommandAction, messageProcessor, queue)))
            );
        });
        return map;
    }

    private Map.Entry<OutboxTaskKey, SimpleTask<OutboxTaskKey>> addTask(SimpleTask<OutboxTaskKey> task) {
        log.info("Add Task: {}", task.key());
        return Map.entry(task.key(), task);
    }
}

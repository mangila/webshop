package com.github.mangila.webshop.outbox.config;

import com.github.mangila.webshop.outbox.application.service.OutboxCommandService;
import com.github.mangila.webshop.outbox.application.service.OutboxQueryService;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.infrastructure.message.MessageProcessor;
import com.github.mangila.webshop.outbox.infrastructure.message.task.*;
import com.github.mangila.webshop.shared.InternalQueue;
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

    private final OutboxQueryService queryService;
    private final MessageProcessor messageProcessor;
    private final OutboxCommandService commandService;

    public OutboxTaskConfig(OutboxQueryService queryService,
                            MessageProcessor messageProcessor,
                            OutboxCommandService commandService) {
        this.queryService = queryService;
        this.messageProcessor = messageProcessor;
        this.commandService = commandService;
    }

    @Bean
    Map<Domain, InternalQueue<OutboxId>> domainToOutboxIdQueue(DomainRegistry domainRegistry) {
        return domainRegistry.keys()
                .stream()
                .peek(domain -> log.info("Create OutboxQueue for domain: {}", domain))
                .collect(Collectors.toMap(Function.identity(), InternalQueue::new));
    }

    @Bean
    Map<OutboxTaskKey, OutboxTask> keyToOutboxTask(Map<Domain, InternalQueue<OutboxId>> domainToOutboxIdQueue) {
        var map = new ConcurrentHashMap<OutboxTaskKey, OutboxTask>();
        domainToOutboxIdQueue.values().forEach(queue -> {
            map.putAll(Map.ofEntries(
                    addTask(new FillQueueOutboxTask(queryService, queue)),
                    addTask(new ProcessQueueOutboxTask(messageProcessor, queue)),
                    addTask(new ProcessDlqOutboxTask(commandService, messageProcessor, queue))
            ));
        });
        return map;
    }

    private Map.Entry<OutboxTaskKey, OutboxTask> addTask(OutboxTask task) {
        log.info("Add OutboxTask: {}", task.key());
        return Map.entry(task.key(), task);
    }

    @Bean
    Map<String, OutboxTaskKey> nameToKey(Map<OutboxTaskKey, OutboxTask> outboxTasks) {
        return outboxTasks.keySet().stream().collect(Collectors.toMap(
                OutboxTaskKey::value,
                Function.identity()
        ));
    }
}

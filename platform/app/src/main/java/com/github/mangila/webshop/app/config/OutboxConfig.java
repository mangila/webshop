package com.github.mangila.webshop.app.config;

import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.OutboxQueryRepository;
import com.github.mangila.webshop.outbox.infrastructure.message.MessageProcessor;
import com.github.mangila.webshop.outbox.infrastructure.message.OutboxDomainMessageQueue;
import com.github.mangila.webshop.outbox.infrastructure.message.task.*;
import com.github.mangila.webshop.shared.model.Domain;
import com.github.mangila.webshop.shared.registry.DomainRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class OutboxConfig {

    private static final Logger log = LoggerFactory.getLogger(OutboxConfig.class);
    private final MessageProcessor processor;
    private final OutboxCommandRepository commandRepository;
    private final OutboxQueryRepository queryRepository;

    public OutboxConfig(MessageProcessor processor,
                        OutboxCommandRepository commandRepository,
                        OutboxQueryRepository queryRepository) {
        this.processor = processor;
        this.commandRepository = commandRepository;
        this.queryRepository = queryRepository;
    }

    @Bean
    Map<Domain, OutboxDomainMessageQueue> domainQueues(DomainRegistry domainRegistry) {
        var map = new ConcurrentHashMap<Domain, OutboxDomainMessageQueue>();
        domainRegistry.keys()
                .stream()
                .peek(domain -> log.info("Create OutboxMessageQueue for domain: {}", domain))
                .forEach(domain -> map.put(domain, new OutboxDomainMessageQueue(domain)));
        return map;
    }

    @Bean
    Map<OutboxTaskKey, OutboxTask> outboxTasks(Map<Domain, OutboxDomainMessageQueue> domainQueues) {
        var map = new ConcurrentHashMap<OutboxTaskKey, OutboxTask>();
        domainQueues.forEach((domain, queue) -> {
            addTask(new FillQueueOutboxTask(queryRepository, queue), map);
            addTask(new ProcessQueueOutboxTask(queue, processor), map);
            addTask(new ProcessDlqOutboxTask(queue, commandRepository, processor), map);
        });
        return map;
    }

    private void addTask(OutboxTask task, ConcurrentHashMap<OutboxTaskKey, OutboxTask> map) {
        map.put(task.key(), task);
        log.info("Created OutboxTask {}", task.key());
    }

    @Bean
    List<OutboxTaskKey> outboxTaskKeysList(Map<OutboxTaskKey, OutboxTask> outboxTasks) {
        return outboxTasks.keySet().stream().toList();
    }

    @Bean
    Map<String, OutboxTaskKey> outboxTaskKeysMap(List<OutboxTaskKey> outboxTaskKeys) {
        return outboxTaskKeys.stream().collect(Collectors.toMap(OutboxTaskKey::value, Function.identity()));
    }
}

package com.github.mangila.webshop.app.config;

import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.OutboxQueryRepository;
import com.github.mangila.webshop.outbox.infrastructure.message.MessageProcessor;
import com.github.mangila.webshop.outbox.infrastructure.message.OutboxQueue;
import com.github.mangila.webshop.outbox.infrastructure.message.task.*;
import com.github.mangila.webshop.shared.model.Domain;
import com.github.mangila.webshop.shared.registry.DomainRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
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
    Map<Domain, OutboxQueue> domainQueues(DomainRegistry domainRegistry) {
        var map = new ConcurrentHashMap<Domain, OutboxQueue>();
        domainRegistry.keys()
                .stream()
                .peek(domain -> log.info("Create OutboxQueue for domain: {}", domain))
                .forEach(domain -> map.put(domain, new OutboxQueue(domain)));
        return map;
    }

    @Bean
    Map<OutboxTaskKey, OutboxTask> outboxTasks(Map<Domain, OutboxQueue> domainQueues) {
        var map = new ConcurrentHashMap<OutboxTaskKey, OutboxTask>();
        domainQueues.forEach((domain, queue) -> {
            addTask(new FillQueueOutboxTask(queryRepository, queue))
                    .andThen(addTask(new ProcessQueueOutboxTask(queue, processor)))
                    .andThen(addTask(new ProcessDlqOutboxTask(queue, commandRepository, processor)))
                    .accept(map);
        });
        return map;
    }

    private Consumer<Map<OutboxTaskKey, OutboxTask>> addTask(OutboxTask task) {
        return map -> {
            map.put(task.key(), task);
            log.info("Created OutboxTask {}", task.key());
        };
    }

    @Bean
    Map<String, OutboxTaskKey> outboxTaskKeys(Map<OutboxTaskKey, OutboxTask> outboxTasks) {
        return outboxTasks.keySet()
                .stream()
                .collect(Collectors.toMap(OutboxTaskKey::value, Function.identity()));
    }
}

package com.github.mangila.webshop.outbox.config;

import com.github.mangila.webshop.outbox.application.action.command.DeleteOutboxCommandAction;
import com.github.mangila.webshop.outbox.application.action.command.UpdateOutboxStatusCommandAction;
import com.github.mangila.webshop.outbox.application.action.query.FindAllOutboxByDomainAndStatusQueryAction;
import com.github.mangila.webshop.outbox.application.action.query.FindAllOutboxIdsByStatusAndDateBeforeQueryAction;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.infrastructure.message.MessageProcessor;
import com.github.mangila.webshop.outbox.infrastructure.task.*;
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

    private final MessageProcessor messageProcessor;

    private final UpdateOutboxStatusCommandAction updateOutboxStatusCommandAction;
    private final FindAllOutboxIdsByStatusAndDateBeforeQueryAction findAllOutboxIdsByStatusAndDateBeforeQueryAction;
    private final FindAllOutboxByDomainAndStatusQueryAction findAllOutboxByDomainAndStatusQueryAction;
    private final DeleteOutboxCommandAction deleteOutboxCommandAction;

    public OutboxTaskConfig(MessageProcessor messageProcessor, UpdateOutboxStatusCommandAction updateOutboxStatusCommandAction, FindAllOutboxIdsByStatusAndDateBeforeQueryAction findAllOutboxIdsByStatusAndDateBeforeQueryAction, FindAllOutboxByDomainAndStatusQueryAction findAllOutboxByDomainAndStatusQueryAction, DeleteOutboxCommandAction deleteOutboxCommandAction) {
        this.messageProcessor = messageProcessor;
        this.updateOutboxStatusCommandAction = updateOutboxStatusCommandAction;
        this.findAllOutboxIdsByStatusAndDateBeforeQueryAction = findAllOutboxIdsByStatusAndDateBeforeQueryAction;
        this.findAllOutboxByDomainAndStatusQueryAction = findAllOutboxByDomainAndStatusQueryAction;
        this.deleteOutboxCommandAction = deleteOutboxCommandAction;
    }

    @Bean
    Map<Domain, InternalQueue<OutboxId>> domainToOutboxIdQueue(DomainRegistry domainRegistry) {
        return domainRegistry.keys()
                .stream()
                .peek(domain -> log.info("Create OutboxQueue for domain: {}", domain))
                .collect(Collectors.toMap(Function.identity(), InternalQueue::new));
    }

    @Bean
    Map<OutboxTaskKey, SimpleTask<OutboxTaskKey>> keyToOutboxTask(Map<Domain, InternalQueue<OutboxId>> domainToOutboxIdQueue) {
        var map = new ConcurrentHashMap<OutboxTaskKey, SimpleTask<OutboxTaskKey>>();
        domainToOutboxIdQueue.values().forEach(queue -> {
            map.putAll(Map.ofEntries(
                    addTask(new FillQueueOutboxTask(findAllOutboxByDomainAndStatusQueryAction, queue)),
                    addTask(new ProcessQueueOutboxTask(messageProcessor, queue)),
                    addTask(new ProcessDlqOutboxTask(updateOutboxStatusCommandAction, messageProcessor, queue)))
            );
        });
        map.putAll(Map.ofEntries(
                addTask(new DeletePublishedOutboxTask(findAllOutboxIdsByStatusAndDateBeforeQueryAction, deleteOutboxCommandAction))
        ));
        return map;
    }

    private Map.Entry<OutboxTaskKey, SimpleTask<OutboxTaskKey>> addTask(SimpleTask<OutboxTaskKey> task) {
        log.info("Add OutboxTask: {}", task.key());
        return Map.entry(task.key(), task);
    }

    @Bean
    Map<String, OutboxTaskKey> nameToOutboxTaskKey(Map<OutboxTaskKey, SimpleTask<OutboxTaskKey>> outboxTasks) {
        return outboxTasks.keySet().stream().collect(Collectors.toMap(
                OutboxTaskKey::value,
                Function.identity()
        ));
    }
}

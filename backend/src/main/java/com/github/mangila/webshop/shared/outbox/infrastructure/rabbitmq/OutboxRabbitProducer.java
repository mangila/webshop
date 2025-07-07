package com.github.mangila.webshop.shared.outbox.infrastructure.rabbitmq;

import com.github.mangila.webshop.shared.application.registry.DomainRegistryService;
import com.github.mangila.webshop.shared.domain.exception.ApplicationException;
import com.github.mangila.webshop.shared.infrastructure.json.JsonMapper;
import com.github.mangila.webshop.shared.outbox.infrastructure.message.OutboxMessage;
import com.rabbitmq.stream.Message;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class OutboxRabbitProducer {

    private final JsonMapper jsonMapper;
    private final DomainRegistryService domainRegistryService;
    private final Map<String, RabbitStreamTemplate> streamTemplates;
    private final Tracer tracer;

    public OutboxRabbitProducer(JsonMapper jsonMapper,
                                DomainRegistryService domainRegistryService,
                                @Qualifier("productStreamTemplate") RabbitStreamTemplate productStreamTemplate,
                                @Qualifier("inventoryStreamTemplate") RabbitStreamTemplate inventoryStreamTemplate, Tracer tracer) {
        this.jsonMapper = jsonMapper;
        this.domainRegistryService = domainRegistryService;
        this.tracer = tracer;
        this.streamTemplates = Map.of(
                "PRODUCT", productStreamTemplate,
                "INVENTORY", inventoryStreamTemplate
        );
    }

    @EventListener(ApplicationReadyEvent.class)
    public void verifyTopics() {
        boolean containsAll = new HashSet<>(domainRegistryService.topics())
                .containsAll(streamTemplates.keySet());
        if (!containsAll) {
            throw new ApplicationException("OutboxEventRabbitMqProducer does not contain all topics");
        }
    }

    public CompletableFuture<Boolean> sendToStream(OutboxMessage outboxMessage) {
        var topic = outboxMessage.topic();
        var type = outboxMessage.event();
        var template = streamTemplates.get(topic);
        template.setObservationEnabled(Boolean.TRUE);
        template.setProducerCustomizer((__, producerBuilder) -> {
            producerBuilder.filterValue(message -> message.getApplicationProperties()
                    .get(topic)
                    .toString());
        });
        Message message = template.messageBuilder()
                .applicationProperties()
                .entry(topic, type)
                .messageBuilder()
                .addData(jsonMapper.toBytes(outboxMessage))
                .build();

        Span customSpan = tracer.nextSpan().name("custom.name").start();
        try (Tracer.SpanInScope scope = tracer.withSpan(customSpan)) {
            customSpan.tag("custom.tag", "value");
            return template.send(message);
        } finally {
            customSpan.end();
        }
    }
}

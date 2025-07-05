package com.github.mangila.webshop.outboxevent.infrastructure.message;

import com.github.mangila.webshop.outboxevent.application.gateway.OutboxEventRegistryGateway;
import com.github.mangila.webshop.outboxevent.domain.OutboxEvent;
import com.github.mangila.webshop.shared.application.json.JsonMapper;
import com.github.mangila.webshop.shared.domain.exception.WebException;
import com.rabbitmq.stream.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class OutboxEventRabbitMqProducer {

    private final JsonMapper jsonMapper;
    private final OutboxEventRegistryGateway outboxEventRegistryGateway;
    private final Map<String, RabbitStreamTemplate> streamTemplates;

    public OutboxEventRabbitMqProducer(JsonMapper jsonMapper,
                                       OutboxEventRegistryGateway outboxEventRegistryGateway,
                                       @Qualifier("productStreamTemplate") RabbitStreamTemplate productStreamTemplate,
                                       @Qualifier("inventoryStreamTemplate") RabbitStreamTemplate inventoryStreamTemplate) {
        this.jsonMapper = jsonMapper;
        this.outboxEventRegistryGateway = outboxEventRegistryGateway;
        this.streamTemplates = Map.of(
                "PRODUCT", productStreamTemplate,
                "INVENTORY", inventoryStreamTemplate
        );
    }

    @EventListener(ApplicationReadyEvent.class)
    public void verifyTopics() {
        boolean containsAll = new HashSet<>(outboxEventRegistryGateway.registry()
                .topics())
                .containsAll(streamTemplates.keySet());
        if (!containsAll) {
            throw new WebException("OutboxEventRabbitMqProducer does not contain all topics", OutboxEvent.class);
        }
    }

    public CompletableFuture<Boolean> convertAndSendToStream(OutboxEvent outboxEvent) {
        var topic = outboxEvent.getTopic();
        var type = outboxEvent.getType();
        var template = streamTemplates.get(topic);
        template.setProducerCustomizer((__, producerBuilder) -> {
            producerBuilder.filterValue(message -> message.getApplicationProperties()
                    .get(topic)
                    .toString());
        });
        Message message = template.messageBuilder()
                .applicationProperties()
                .entry(topic, type)
                .messageBuilder()
                .addData(jsonMapper.toBytes(outboxEvent))
                .build();
        return template.send(message);
    }
}

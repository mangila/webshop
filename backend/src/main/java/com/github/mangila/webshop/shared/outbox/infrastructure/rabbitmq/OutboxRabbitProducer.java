package com.github.mangila.webshop.shared.outbox.infrastructure.rabbitmq;

import com.github.mangila.webshop.shared.domain.exception.ApplicationException;
import com.github.mangila.webshop.shared.infrastructure.json.JsonMapper;
import com.github.mangila.webshop.shared.outbox.application.gateway.OutboxRegistryGateway;
import com.github.mangila.webshop.shared.outbox.infrastructure.message.OutboxMessage;
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
public class OutboxRabbitProducer {

    private final JsonMapper jsonMapper;
    private final OutboxRegistryGateway registryGateway;
    private final Map<String, RabbitStreamTemplate> streamTemplates;

    public OutboxRabbitProducer(JsonMapper jsonMapper,
                                OutboxRegistryGateway outboxEventRegistryGateway,
                                @Qualifier("productStreamTemplate") RabbitStreamTemplate productStreamTemplate,
                                @Qualifier("inventoryStreamTemplate") RabbitStreamTemplate inventoryStreamTemplate) {
        this.jsonMapper = jsonMapper;
        this.registryGateway = outboxEventRegistryGateway;
        this.streamTemplates = Map.of(
                "PRODUCT", productStreamTemplate,
                "INVENTORY", inventoryStreamTemplate
        );
    }

    @EventListener(ApplicationReadyEvent.class)
    public void verifyTopics() {
        boolean containsAll = new HashSet<>(registryGateway.registry()
                .topics())
                .containsAll(streamTemplates.keySet());
        if (!containsAll) {
            throw new ApplicationException("OutboxEventRabbitMqProducer does not contain all topics");
        }
    }

    public CompletableFuture<Boolean> sendToStream(OutboxMessage outboxMessage) {
        var topic = outboxMessage.topic();
        var type = outboxMessage.event();
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
                .addData(jsonMapper.toBytes(outboxMessage))
                .build();
        return template.send(message);
    }

}

package com.github.mangila.webshop.inventory.infrastructure.config;

import com.github.mangila.webshop.shared.infrastructure.config.RabbitMqConfig;
import com.github.mangila.webshop.shared.outbox.application.gateway.OutboxRegistryGateway;
import com.rabbitmq.stream.Environment;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.rabbit.stream.config.StreamRabbitListenerContainerFactory;
import org.springframework.rabbit.stream.listener.StreamListenerContainer;

@DependsOn("productEventRegistryConfig")
@Configuration
public class InventoryRabbitMqConfig {

    private final OutboxRegistryGateway outboxRegistryGateway;

    public InventoryRabbitMqConfig(OutboxRegistryGateway outboxRegistryGateway) {
        this.outboxRegistryGateway = outboxRegistryGateway;
    }

    @Bean
    public RabbitListenerContainerFactory<StreamListenerContainer> inventoryNewProductConsumer(Environment env) {
        var streamKey = RabbitMqConfig.OUTBOX_EVENT_PRODUCT_STREAM_KEY;
        var topic = "PRODUCT";
        var type = "PRODUCT_CREATE_NEW";
        outboxRegistryGateway.registry().ensureHasTopicAndTypeRegistered(topic, type);
        StreamRabbitListenerContainerFactory factory = new StreamRabbitListenerContainerFactory(env);
        factory.setNativeListener(Boolean.TRUE);
        factory.setConsumerCustomizer((id, builder) -> builder.name("inventoryNewProductConsumer")
                .stream(streamKey)
                .filter()
                .values(type)
                .postFilter(message -> type.equals(message.getApplicationProperties().get(topic)))
                .builder()
                .autoTrackingStrategy());
        return factory;
    }

}

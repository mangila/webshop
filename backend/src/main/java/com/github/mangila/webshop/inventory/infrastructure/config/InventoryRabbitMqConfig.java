package com.github.mangila.webshop.inventory.infrastructure.config;

import com.github.mangila.webshop.shared.infrastructure.RabbitMqConfig;
import com.github.mangila.webshop.outboxevent.application.gateway.OutboxEventRegistryGateway;
import com.rabbitmq.stream.Environment;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.adapter.MessagingMessageListenerAdapter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.rabbit.stream.config.StreamRabbitListenerContainerFactory;
import org.springframework.rabbit.stream.listener.StreamListenerContainer;

@DependsOn("productEventRegistryConfig")
@Configuration
public class InventoryRabbitMqConfig {

    private final OutboxEventRegistryGateway outboxEventRegistryGateway;

    public InventoryRabbitMqConfig(OutboxEventRegistryGateway outboxEventRegistryGateway) {
        this.outboxEventRegistryGateway = outboxEventRegistryGateway;
    }

    @Bean
    public RabbitListenerContainerFactory<StreamListenerContainer> inventoryNewProductConsumer(Environment env,
                                                                                               @Qualifier("outboxEventMessageConverter") MessageConverter messageConverter) {
        var streamKey = RabbitMqConfig.OUTBOX_EVENT_PRODUCT_STREAM_KEY;
        var topic = "PRODUCT";
        var type = "PRODUCT_CREATE_NEW";
        outboxEventRegistryGateway.registry().hasTopicAndTypeRegistered(topic, type);
        StreamRabbitListenerContainerFactory factory = new StreamRabbitListenerContainerFactory(env);
        factory.setNativeListener(Boolean.TRUE);
        factory.setContainerCustomizer(container -> {
            var listener = (MessagingMessageListenerAdapter) container.getMessageListener();
            listener.setMessageConverter(messageConverter);
        });
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

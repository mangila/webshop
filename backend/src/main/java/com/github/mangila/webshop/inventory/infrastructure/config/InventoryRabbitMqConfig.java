package com.github.mangila.webshop.inventory.infrastructure.config;

import com.github.mangila.webshop.shared.application.registry.Domain;
import com.github.mangila.webshop.shared.application.registry.Event;
import com.github.mangila.webshop.shared.application.registry.RegistryService;
import com.github.mangila.webshop.shared.infrastructure.config.RabbitMqConfig;
import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.Message;
import io.micrometer.common.KeyValues;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.rabbit.stream.config.StreamRabbitListenerContainerFactory;
import org.springframework.rabbit.stream.listener.StreamListenerContainer;
import org.springframework.rabbit.stream.micrometer.RabbitStreamListenerObservationConvention;
import org.springframework.rabbit.stream.micrometer.RabbitStreamMessageReceiverContext;

import java.util.function.Predicate;

@DependsOn("productDomainRegistryConfig")
@Configuration
public class InventoryRabbitMqConfig {

    private final RegistryService registryService;

    public InventoryRabbitMqConfig(RegistryService registryService) {
        this.registryService = registryService;
    }

    @Bean
    public RabbitListenerContainerFactory<StreamListenerContainer> inventoryNewProductConsumerFactory(Environment environment) {
        var streamKey = RabbitMqConfig.PRODUCT_STREAM_KEY;
        var domain = Domain.from("PRODUCT", registryService);
        var event = Event.from("PRODUCT_CREATE_NEW", registryService);
        StreamRabbitListenerContainerFactory factory = new StreamRabbitListenerContainerFactory(environment);
        factory.setNativeListener(Boolean.TRUE);
        factory.setObservationEnabled(Boolean.TRUE);
        factory.setStreamListenerObservationConvention(new RabbitStreamListenerObservationConvention() {
            @Override
            public String getName() {
                return "rabbit-mq-stream-listener";
            }

            @Override
            public String getContextualName(RabbitStreamMessageReceiverContext context) {
                return context.getSource().concat(": ").concat(context.getListenerId());
            }

            @Override
            public KeyValues getLowCardinalityKeyValues(RabbitStreamMessageReceiverContext context) {
                return KeyValues.of("domain", domain.value()).and("event", event.value());
            }
        });
        factory.setConsumerCustomizer((_, builder) -> builder.name("inventoryNewProductConsumer")
                .stream(streamKey)
                .filter()
                .values(domain.value())
                .postFilter(filterOnDomain(domain))
                .values(event.value())
                .postFilter(filterOnEvent(event))
                .builder()
                .autoTrackingStrategy());
        return factory;
    }

    private Predicate<Message> filterOnDomain(Domain domain) {
        return message -> domain.value().equals(message.getApplicationProperties().get("domain"));
    }

    private Predicate<Message> filterOnEvent(Event event) {
        return message -> event.value().equals(message.getApplicationProperties().get("event"));
    }
}

package com.github.mangila.webshop.inventory.infrastructure.config;

import com.github.mangila.webshop.shared.application.registry.DomainKey;
import com.github.mangila.webshop.shared.application.registry.EventKey;
import com.github.mangila.webshop.shared.application.registry.RegistryService;
import com.github.mangila.webshop.shared.infrastructure.config.RabbitMqConfig;
import com.rabbitmq.stream.Environment;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.rabbit.stream.config.StreamRabbitListenerContainerFactory;
import org.springframework.rabbit.stream.listener.StreamListenerContainer;
import org.springframework.rabbit.stream.micrometer.RabbitStreamListenerObservation;

@DependsOn("productRegistryConfig")
@Configuration
public class InventoryRabbitMqConfig {

    private final RegistryService registryService;

    public InventoryRabbitMqConfig(RegistryService registryService) {
        this.registryService = registryService;
    }

    @Bean
    public RabbitListenerContainerFactory<StreamListenerContainer> inventoryNewProductConsumer(Environment env) {
        var streamKey = RabbitMqConfig.PRODUCT_STREAM_KEY;
        var domain = "PRODUCT";
        var event = "PRODUCT_CREATE_NEW";
        registryService.ensureDomainIsRegistered(DomainKey.from(domain));
        registryService.ensureEventIsRegistered(EventKey.from(event));
        StreamRabbitListenerContainerFactory factory = new StreamRabbitListenerContainerFactory(env);
        factory.setNativeListener(Boolean.TRUE);
        factory.setObservationEnabled(Boolean.TRUE);
        factory.setStreamListenerObservationConvention(new RabbitStreamListenerObservation.DefaultRabbitStreamListenerObservationConvention());
        factory.setConsumerCustomizer((_, builder) -> builder.name("inventoryNewProductConsumer")
                .stream(streamKey)
                .filter()
                .values(event)
                .postFilter(message -> event.equals(message.getApplicationProperties().get(domain)))
                .builder()
                .autoTrackingStrategy());
        return factory;
    }

}

package com.github.mangila.webshop.backend.common.config;

import com.rabbitmq.stream.Address;
import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.EnvironmentBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.rabbit.stream.support.StreamAdmin;

@Configuration
@EnableRabbit
public class RabbitMqConfig {

    public static final String OUTBOX_EVENT_PRODUCT_STREAM_KEY = "product.event.stream";
    public static final String OUTBOX_EVENT_INVENTORY_STREAM_KEY = "inventory.event.stream";

    /**
     * https://rabbitmq.github.io/rabbitmq-stream-java-client/stable/htmlsingle/#understanding-connection-logic
     */
    @Bean
    public Environment rabbitStreamEnvironment(RabbitProperties properties) {
        EnvironmentBuilder builder = Environment.builder();
        var streamProps = properties.getStream();
        builder.addressResolver(address -> new Address(streamProps.getHost(), streamProps.getPort()));
        builder.username(streamProps.getUsername())
                .password(streamProps.getPassword());
        return builder.build();
    }

    @Bean
    public StreamAdmin streamAdmin(Environment env) {
        return new StreamAdmin(env, sc -> {
            sc.stream(OUTBOX_EVENT_PRODUCT_STREAM_KEY).create();
            sc.stream(OUTBOX_EVENT_INVENTORY_STREAM_KEY).create();
        });
    }

    @Bean
    public MessageConverter outboxEventMessageConverter() {
        SimpleMessageConverter converter = new SimpleMessageConverter();
        converter.addAllowedListPatterns("com.github.mangila.webshop.backend.outboxevent.domain.*");
        return converter;
    }
}

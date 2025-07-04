package com.github.mangila.webshop.backend.outboxevent.infrastructure.config;

import com.rabbitmq.stream.Environment;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate;
import org.springframework.rabbit.stream.support.StreamAdmin;

@Configuration
public class OutboxEventRabbitMqConfig {

    public static final String OUTBOX_EVENT_STREAM_KEY = "event.stream";

    @Bean
    public StreamAdmin outboxEventstreamAdmin(Environment env) {
        return new StreamAdmin(env, sc -> sc.stream(OUTBOX_EVENT_STREAM_KEY).create());
    }

    @Bean
    public RabbitStreamTemplate outboxEventRabbitStreamTemplate(Environment env) {
        var template = new RabbitStreamTemplate(env, OUTBOX_EVENT_STREAM_KEY);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public MessageConverter messageConverter() {
        SimpleMessageConverter converter = new SimpleMessageConverter();
        converter.addAllowedListPatterns("com.github.mangila.webshop.backend.outboxevent.domain.*");
        return converter;
    }

}

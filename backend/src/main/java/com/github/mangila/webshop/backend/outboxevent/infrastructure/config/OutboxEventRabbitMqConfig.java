package com.github.mangila.webshop.backend.outboxevent.infrastructure.config;

import com.github.mangila.webshop.backend.common.config.RabbitMqConfig;
import com.rabbitmq.stream.Environment;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate;

@Configuration
public class OutboxEventRabbitMqConfig {

    @Bean
    public RabbitStreamTemplate productStreamTemplate(Environment env,
                                                      @Qualifier("outboxEventMessageConverter") MessageConverter messageConverter) {
        var template = new RabbitStreamTemplate(env, RabbitMqConfig.OUTBOX_EVENT_PRODUCT_STREAM_KEY);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean
    public RabbitStreamTemplate inventoryStreamTemplate(Environment env,
                                                        @Qualifier("outboxEventMessageConverter") MessageConverter messageConverter) {
        var template = new RabbitStreamTemplate(env, RabbitMqConfig.OUTBOX_EVENT_INVENTORY_STREAM_KEY);
        template.setMessageConverter(messageConverter);
        return template;
    }
}

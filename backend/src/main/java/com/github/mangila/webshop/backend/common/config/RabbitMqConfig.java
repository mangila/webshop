package com.github.mangila.webshop.backend.common.config;

import com.rabbitmq.stream.Address;
import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.EnvironmentBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.adapter.MessagingMessageListenerAdapter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.rabbit.stream.config.StreamRabbitListenerContainerFactory;
import org.springframework.rabbit.stream.listener.StreamListenerContainer;

@Configuration
@EnableRabbit
public class RabbitMqConfig {

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
    public RabbitListenerContainerFactory<StreamListenerContainer> c1(Environment env) {
        StreamRabbitListenerContainerFactory factory = new StreamRabbitListenerContainerFactory(env);
        // factory.setNativeListener(true);
        factory.setContainerCustomizer(container -> {
            var l = (MessagingMessageListenerAdapter) container.getMessageListener();
            var m = new SimpleMessageConverter();
            m.addAllowedListPatterns("*");
            //   m.addAllowedListPatterns("com.github.mangila.webshop.backend.outboxevent.domain.*");
            l.setMessageConverter(m);
        });
        factory.setConsumerCustomizer((id, builder) -> {
            builder.name("myConsumer")
                    .stream("event.stream")
                    .manualTrackingStrategy();
        });
        return factory;
    }
//
//    @Bean
//    public RabbitListenerContainerFactory<StreamListenerContainer> c2(Environment env) {
//        StreamRabbitListenerContainerFactory factory = new StreamRabbitListenerContainerFactory(env);
//        factory.setNativeListener(true);
//        factory.setConsumerCustomizer((id, builder) -> {
//            builder.name("myConsumer2").manualTrackingStrategy();
//        });
//        return factory;
//    }
}

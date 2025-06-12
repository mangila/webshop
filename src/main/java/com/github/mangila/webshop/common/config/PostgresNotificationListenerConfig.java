package com.github.mangila.webshop.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.webshop.common.PgNotificationListener;
import com.github.mangila.webshop.common.SingleConnectionJdbcTemplate;
import com.github.mangila.webshop.common.model.ChannelTopic;
import com.github.mangila.webshop.product.model.ProductNotification;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.time.Duration;

@Configuration
public class PostgresNotificationListenerConfig {

    @Bean
    @Scope("prototype")
    public SingleConnectionJdbcTemplate singleConnectionJdbcTemplateProtoType(DataSourceProperties props) {
        return new SingleConnectionJdbcTemplate(props);
    }

    @Bean
    public PgNotificationListener productNotificationListener(
            ApplicationEventPublisher publisher,
            ObjectMapper objectMapper,
            @Qualifier("singleConnectionJdbcTemplateProtoType") SingleConnectionJdbcTemplate template
    ) {
        return new PgNotificationListener(
                ChannelTopic.PRODUCTS,
                Duration.ofMillis(500),
                ProductNotification.class,
                publisher,
                objectMapper,
                template
        );
    }

}

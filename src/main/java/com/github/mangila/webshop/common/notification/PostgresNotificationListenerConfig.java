package com.github.mangila.webshop.common.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.webshop.common.SingleConnectionJdbcTemplate;
import com.github.mangila.webshop.common.event.EventTopic;
import com.github.mangila.webshop.common.notification.model.ProductNotification;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.time.Duration;

@Configuration
public class PostgresNotificationListenerConfig {

    @Bean
    @Scope("prototype")
    public SingleConnectionJdbcTemplate singleConnectionJdbcTemplateProtoType(HikariDataSource dataSource) {
        var singleConnectionDataSource = new SingleConnectionDataSource(
                dataSource.getJdbcUrl(),
                dataSource.getUsername(),
                dataSource.getPassword(),
                Boolean.TRUE
        );
        return new SingleConnectionJdbcTemplate(singleConnectionDataSource);
    }

    @Bean
    public PostgresListener productPostgresNotificationListener(
            NotificationPublisher publisher,
            ObjectMapper objectMapper,
            SingleConnectionJdbcTemplate template
    ) {
        return new PostgresNotificationListener(
                publisher,
                objectMapper,
                template.getTemplate(),
                EventTopic.PRODUCT,
                Duration.ofMillis(500),
                ProductNotification.class
        );
    }

    @Bean
    public PostgresListener inventoryPostgresNotificationListener(
            NotificationPublisher publisher,
            ObjectMapper objectMapper,
            SingleConnectionJdbcTemplate template
    ) {
        return new PostgresNotificationListener(
                publisher,
                objectMapper,
                template.getTemplate(),
                EventTopic.INVENTORY,
                Duration.ofMillis(500),
                ProductNotification.class
        );
    }

}

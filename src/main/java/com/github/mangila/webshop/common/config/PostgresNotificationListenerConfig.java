package com.github.mangila.webshop.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.webshop.common.PgNotificationListener;
import com.github.mangila.webshop.common.SingleConnectionJdbcTemplate;
import com.github.mangila.webshop.common.model.ChannelTopic;
import com.github.mangila.webshop.product.model.ProductNotification;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.ApplicationEventPublisher;
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
        var scds = new SingleConnectionDataSource(
                dataSource.getJdbcUrl(),
                dataSource.getUsername(),
                dataSource.getPassword(),
                Boolean.TRUE
        );
        return new SingleConnectionJdbcTemplate(scds);
    }

    @Bean
    public PgNotificationListener productPgNotificationListener(
            ApplicationEventPublisher publisher,
            ObjectMapper objectMapper,
            SingleConnectionJdbcTemplate template
    ) {
        return new PgNotificationListener(
                ChannelTopic.PRODUCTS,
                Duration.ofMillis(500),
                ProductNotification.class,
                publisher,
                objectMapper,
                template.getTemplate()
        );
    }

}

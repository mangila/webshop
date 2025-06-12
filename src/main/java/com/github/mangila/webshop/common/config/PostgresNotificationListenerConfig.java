package com.github.mangila.webshop.common.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

@Configuration
public class PostgresNotificationListenerConfig {

    @Bean
    public JdbcTemplate singleConnectionJdbcTemplate(DataSourceProperties props) {
        var sdb = props.initializeDataSourceBuilder()
                .type(SingleConnectionDataSource.class)
                .build();
        return new JdbcTemplate(sdb);
    }
}

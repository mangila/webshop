package com.github.mangila.webshop.common.config;

import com.github.mangila.webshop.common.SingleConnectionJdbcTemplate;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class PostgresNotificationListenerConfig {

    @Bean(destroyMethod = "destroy")
    @Scope("prototype")
    public SingleConnectionJdbcTemplate singleConnectionJdbcTemplateProtoType(DataSourceProperties props) {
        return new SingleConnectionJdbcTemplate(props);
    }
}

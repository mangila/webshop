package com.github.mangila.webshop.common;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public class SingleConnectionJdbcTemplate {

    private final JdbcTemplate jdbcTemplate;

    public SingleConnectionJdbcTemplate(SingleConnectionDataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public JdbcTemplate getTemplate() {
        return jdbcTemplate;
    }
}

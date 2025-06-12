package com.github.mangila.webshop.common;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Service;

@Service
public class SingleConnectionJdbcTemplate implements DisposableBean {

    private final JdbcTemplate jdbcTemplate;

    public SingleConnectionJdbcTemplate(DataSourceProperties props) {
        var sds = props.initializeDataSourceBuilder()
                .type(SingleConnectionDataSource.class)
                .build();
        sds.setSuppressClose(false);
        this.jdbcTemplate = new JdbcTemplate(sds);
    }

    public JdbcTemplate getTemplate() {
        return jdbcTemplate;
    }

    @Override
    public void destroy() throws Exception {
        if (jdbcTemplate.getDataSource() != null) {
            jdbcTemplate.getDataSource()
                    .getConnection()
                    .close();
        }
    }
}

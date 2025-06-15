package com.github.mangila.webshop.common.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class PostgresConfig {

    private static final Logger log = LoggerFactory.getLogger(PostgresConfig.class);

    private final JdbcTemplate jdbc;

    public PostgresConfig(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @PostConstruct
    public void init() {
        newInventoryAfterProductInsert();
        log.info("Postgres function/trigger configuration complete");
    }

    private void newInventoryAfterProductInsert() {
        log.info("Creating Postgres function new_inventory_after_product_insert()");
        jdbc.execute("""
                DROP FUNCTION IF EXISTS new_inventory_after_product_insert();
                CREATE OR REPLACE FUNCTION new_inventory_after_product_insert() RETURNS TRIGGER AS $$
                BEGIN
                INSERT INTO inventory (product_id)
                     VALUES (NEW.id);
                RETURN NEW;
                END;
                $$ LANGUAGE plpgsql;
                """);
        jdbc.execute("""
                DROP TRIGGER IF EXISTS new_inventory_after_product_insert ON event;
                CREATE TRIGGER new_inventory_after_product_insert
                AFTER INSERT ON product
                FOR EACH ROW
                EXECUTE FUNCTION new_inventory_after_product_insert();
                """);
    }
}

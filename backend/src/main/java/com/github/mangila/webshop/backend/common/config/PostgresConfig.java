package com.github.mangila.webshop.backend.common.config;

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
        updateUpdatedColumn();
        log.info("Postgres function/trigger configuration complete");
    }

    private void newInventoryAfterProductInsert() {
        log.info("Creating Postgres function new_inventory_after_product_insert()");
        // language=PostgreSQL
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
        log.info("Creating Postgres trigger new_inventory_after_product_insert on product table");
        // language=PostgreSQL
        jdbc.execute("""
                DROP TRIGGER IF EXISTS new_inventory_after_product_insert ON product;
                """);
        // language=PostgreSQL
        jdbc.execute("""
                CREATE TRIGGER new_inventory_after_product_insert
                AFTER INSERT ON product
                FOR EACH ROW
                EXECUTE FUNCTION new_inventory_after_product_insert();
                """);
    }

    public void updateUpdatedColumn() {
        log.info("Creating Postgres function update_updated_column()");
        // language=PostgreSQL
        jdbc.execute("""
                 DROP FUNCTION IF EXISTS update_updated_column();
                """);
        // language=PostgreSQL
        jdbc.execute("""
                CREATE OR REPLACE FUNCTION update_updated_column()
                RETURNS TRIGGER AS $$
                BEGIN
                   NEW.updated = CURRENT_TIMESTAMP;
                   RETURN NEW;
                END;
                $$ LANGUAGE plpgsql;
                """);
        log.info("Creating Postgres trigger update_updated_column_product on product table");
        // language=PostgreSQL
        jdbc.execute("""
                DROP TRIGGER IF EXISTS update_updated_column_product ON product;
                """);
        // language=PostgreSQL
        jdbc.execute("""
                CREATE TRIGGER update_updated_column_product
                BEFORE UPDATE ON product
                FOR EACH ROW
                EXECUTE FUNCTION update_updated_column();
                """);
    }
}

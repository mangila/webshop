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
        createTriggerNewProductInventory();
        createFunctionUpdateUpdatedColumn();
        log.info("Postgres function/trigger configuration complete");
    }

    private void createTriggerNewProductInventory() {
        log.info("Creating Postgres function trgfn_inventory_insert() and trfn_inventory_delete()");
        // language=PostgreSQL
        jdbc.execute("""
                DROP FUNCTION IF EXISTS trgfn_inventory_insert();
                """);
        // language=PostgreSQL
        jdbc.execute("""
                CREATE OR REPLACE FUNCTION trgfn_inventory_insert() RETURNS TRIGGER AS $$
                BEGIN
                INSERT INTO inventory (product_id)
                     VALUES (NEW.id);
                RETURN NEW;
                END;
                $$ LANGUAGE plpgsql;
                """);
        // language=PostgreSQL
        jdbc.execute("""
                DROP TRIGGER IF EXISTS trg_product_after_insert ON product;
                """);
        // language=PostgreSQL
        jdbc.execute("""
                CREATE TRIGGER trg_product_after_insert_01
                AFTER INSERT ON product
                FOR EACH ROW
                EXECUTE FUNCTION trgfn_inventory_insert();
                """);
        // language=PostgreSQL
        jdbc.execute("""
                 DROP FUNCTION IF EXISTS trfn_inventory_delete();
                """);
        // language=PostgreSQL
        jdbc.execute("""
                CREATE OR REPLACE FUNCTION trfn_inventory_delete()
                RETURNS TRIGGER AS $$
                BEGIN
                    DELETE FROM inventory WHERE product_id = OLD.id;
                    RETURN OLD;
                END;
                $$ LANGUAGE plpgsql;
                """);
        // language=PostgreSQL
        jdbc.execute("""
                CREATE TRIGGER trg_product_after_delete_01
                AFTER DELETE ON product
                FOR EACH ROW
                EXECUTE FUNCTION trfn_inventory_delete();
                """);
    }

    public void createFunctionUpdateUpdatedColumn() {
        log.info("Creating Postgres function trgfn_update_updated_column() and trigger trg_product_after_update_01 on product table");
        // language=PostgreSQL
        jdbc.execute("""
                 DROP FUNCTION IF EXISTS trgfn_update_updated_column();
                """);
        // language=PostgreSQL
        jdbc.execute("""
                CREATE OR REPLACE FUNCTION trgfn_update_updated_column()
                RETURNS TRIGGER AS $$
                BEGIN
                   NEW.updated = CURRENT_TIMESTAMP;
                   RETURN NEW;
                END;
                $$ LANGUAGE plpgsql;
                """);
        // language=PostgreSQL
        jdbc.execute("""
                DROP TRIGGER IF EXISTS trg_product_after_update_01 ON product;
                """);
        // language=PostgreSQL
        jdbc.execute("""
                CREATE TRIGGER trg_product_after_update_01
                AFTER UPDATE ON product
                FOR EACH ROW
                EXECUTE FUNCTION trgfn_update_updated_column();
                """);
    }
}

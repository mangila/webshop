package com.github.mangila.webshop.product.query;

import com.github.mangila.webshop.product.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProductQueryRepository {

    private static final Logger log = LoggerFactory.getLogger(ProductQueryRepository.class);
    private final JdbcTemplate jdbc;

    public ProductQueryRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Product queryById(String id) {
        final String sql = """
                SELECT id, name, description, price, image_url, category, created, updated, extensions
                FROM product WHERE id = ?
                """;
        log.debug("{} -- {}", id, sql);
        try {
            return jdbc.queryForObject(sql, new BeanPropertyRowMapper<>(Product.class), id);
        } catch (Exception e) {
            var msg = "Failed to query product with id -- %s".formatted(id);
            log.error(msg, e);
            throw new RuntimeException(msg);
        }
    }
}

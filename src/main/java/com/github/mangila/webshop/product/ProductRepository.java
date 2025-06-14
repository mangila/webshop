package com.github.mangila.webshop.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.webshop.product.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

    private static final Logger log = LoggerFactory.getLogger(ProductRepository.class);

    private final JdbcTemplate jdbc;
    private final ObjectMapper objectMapper;

    public ProductRepository(JdbcTemplate jdbc,
                             ObjectMapper objectMapper) {
        this.jdbc = jdbc;
        this.objectMapper = objectMapper;
    }

    public Product queryById(String id) {
        return new Product();
    }

    public void insertNew(Product product) {
        final String sql = """
                INSERT INTO product (id, name, description, price, image_url, category, extensions)
                VALUES (?, ?, ?, ?, ?, ?, ?::jsonb)
                ON CONFLICT (id)
                DO UPDATE SET
                id = EXCLUDED.id,
                name = EXCLUDED.name,
                description = EXCLUDED.description,
                price = EXCLUDED.price,
                image_url = EXCLUDED.image_url,
                category = EXCLUDED.category,
                extensions = EXCLUDED.extensions
                """;
        log.trace("{} -- {}", product, sql);
        jdbc.update(sql, ps -> {
            ps.setString(1, product.getId());
            ps.setString(2, product.getName());
            ps.setString(3, product.getDescription());
            ps.setBigDecimal(4, product.getPrice());
            ps.setString(5, product.getImageUrl());
            ps.setString(6, product.getCategory());
            ps.setString(7, product.getExtensions() != null ? product.getExtensions() : "{}");
        });
    }

    public boolean deleteProduct(String id) {
        final String sql = """
                DELETE FROM product WHERE id = ?
                """;
        return jdbc.update(sql, id) == 1;
    }
}

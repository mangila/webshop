package com.github.mangila.webshop.product.command;

import com.github.mangila.webshop.common.util.JsonUtils;
import com.github.mangila.webshop.product.ProductRepositoryMapper;
import com.github.mangila.webshop.product.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;

@Repository
public class ProductCommandRepository {

    private static final Logger log = LoggerFactory.getLogger(ProductCommandRepository.class);

    private final JdbcTemplate jdbc;
    private final ProductRepositoryMapper repositoryMapper;

    public ProductCommandRepository(JdbcTemplate jdbc,
                                    ProductRepositoryMapper repositoryMapper) {
        this.jdbc = jdbc;
        this.repositoryMapper = repositoryMapper;
    }

    public Product upsertProduct(Product p) {
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
                updated = CURRENT_TIMESTAMP,
                extensions = EXCLUDED.extensions
                RETURNING id, name, description, price, image_url, category, created, updated, extensions
                """;
        log.debug("{} -- {}", p, sql);
        var params = new Object[]{
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                p.getImageUrl(),
                p.getCategory(),
                p.getExtensions() != null ? p.getExtensions() : JsonUtils.EMPTY_JSON
        };
        return jdbc.queryForObject(sql, repositoryMapper.getProductRowMapper(), params);
    }

    public void deleteProductById(String id) {
        final String sql = """
                DELETE FROM product WHERE id = ?
                """;
        log.debug("{} -- {}", id, sql);
        try {
            jdbc.update(sql, id);
        } catch (Exception e) {
            var msg = "Failed to delete product with id -- %s".formatted(id);
            log.error(msg, e);
            throw new RuntimeException(msg);
        }
    }

    public Product update(String id, Object data, String fieldName) {
        final String sql = """
                UPDATE product
                SET
                %s = ?,
                updated = ?
                WHERE id = ?
                RETURNING id, name, description, price, image_url, category, created, updated, extensions
                """.formatted(fieldName);
        log.debug("{} -- {} -- {}", id, data, sql);
        try {
            return jdbc.queryForObject(sql,
                    repositoryMapper.getProductRowMapper(),
                    data,
                    Timestamp.from(Instant.now()),
                    id);
        } catch (Exception e) {
            var msg = "Failed to update product with id -- %s -- %s -- %s".formatted(id, fieldName, data);
            log.error(msg, e);
            throw new RuntimeException(msg);
        }
    }
}

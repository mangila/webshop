package com.github.mangila.webshop.product.command;

import com.github.mangila.webshop.product.ProductMapper;
import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.product.model.ProductEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

@Repository
public class ProductCommandRepository {

    private static final Logger log = LoggerFactory.getLogger(ProductCommandRepository.class);

    private final ProductMapper productMapper;
    private final JdbcTemplate jdbc;

    public ProductCommandRepository(ProductMapper productMapper,
                                    JdbcTemplate jdbc) {
        this.productMapper = productMapper;
        this.jdbc = jdbc;
    }

    public Optional<Product> upsertProduct(ProductEntity p) {
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
        var params = new Object[]{
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                p.getImageUrl(),
                p.getCategory(),
                p.getExtensions(),
        };
        log.debug("{} -- {}", Arrays.toString(params), sql);
        var result = jdbc.query(sql,
                new BeanPropertyRowMapper<>(ProductEntity.class),
                params);
        if (CollectionUtils.isEmpty(result)) {
            return Optional.empty();
        }
        var product = productMapper.toProduct(result.getFirst());
        return Optional.of(product);
    }

    public Optional<Product> deleteProductById(String id) {
        try {
            final String sql = """
                    DELETE FROM product WHERE id = ?
                    RETURNING id, name, description, price, image_url, category, created, updated, extensions
                    """;
            log.debug("{} -- {}", id, sql);
            var entity = jdbc.queryForObject(sql,
                    new BeanPropertyRowMapper<>(ProductEntity.class),
                    id);
            if (entity == null) {
                return Optional.empty();
            }
            return Optional.of(productMapper.toProduct(entity));
        } catch (Exception e) {
            var msg = "Failed to delete product with id -- %s".formatted(id);
            log.error(msg, e);
            throw new RuntimeException(msg);
        }
    }

    public Optional<Product> updateOneField(String id, String fieldName, Object data) {
        try {
            final String sql = """
                    UPDATE product
                    SET
                    %s = ?,
                    updated = ?
                    WHERE id = ?
                    RETURNING id, name, description, price, image_url, category, created, updated, extensions
                    """.formatted(fieldName);
            var params = new Object[]{data, Timestamp.from(Instant.now()), id};
            log.debug("{} -- {}", Arrays.toString(params), sql);
            var entity = jdbc.queryForObject(sql,
                    new BeanPropertyRowMapper<>(ProductEntity.class),
                    params);
            if (entity == null) {
                return Optional.empty();
            }
            return Optional.of(productMapper.toProduct(entity));
        } catch (Exception e) {
            var msg = "Failed to update product with id -- %s -- %s -- %s".formatted(id, fieldName, data);
            log.error(msg, e);
            throw new RuntimeException(msg);
        }
    }
}

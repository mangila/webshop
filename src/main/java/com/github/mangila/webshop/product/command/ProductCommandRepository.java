package com.github.mangila.webshop.product.command;

import com.github.mangila.webshop.product.ProductMapper;
import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.product.model.ProductEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.time.Instant;
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
                INSERT INTO product (id, name, price, attributes)
                VALUES (?, ?, ?, ?::jsonb)
                ON CONFLICT (id)
                DO UPDATE SET
                id = EXCLUDED.id,
                name = EXCLUDED.name,
                price = EXCLUDED.price,
                attributes = EXCLUDED.attributes
                RETURNING id, name, price, created, updated, attributes
                """;
        var params = new Object[]{
                p.id(),
                p.name(),
                p.price(),
                p.attributes(),
        };
        var result = jdbc.query(sql,
                productMapper.getRowMapper(),
                params);
        if (CollectionUtils.isEmpty(result)) {
            return Optional.empty();
        }
        var product = productMapper.toProduct(result.getFirst());
        return Optional.of(product);
    }

    public Optional<Product> deleteProductById(String id) {
        final String sql = """
                DELETE FROM product WHERE id = ?
                RETURNING id, name, price, created, updated, attributes
                """;
        var result = jdbc.query(sql,
                productMapper.getRowMapper(),
                id);
        if (CollectionUtils.isEmpty(result)) {
            return Optional.empty();
        }
        var product = productMapper.toProduct(result.getFirst());
        return Optional.of(product);
    }

    public Optional<Product> updateOneField(String id, String fieldName, Object data) {
        final String sql = """
                UPDATE product
                SET
                %s = ?,
                updated = ?
                WHERE id = ?
                RETURNING id, name, price, created, updated, attributes
                """.formatted(fieldName);
        var params = new Object[]{data, Timestamp.from(Instant.now()), id};
        var result = jdbc.query(sql,
                productMapper.getRowMapper(),
                params);
        if (CollectionUtils.isEmpty(result)) {
            return Optional.empty();
        }
        var product = productMapper.toProduct(result.getFirst());
        return Optional.of(product);
    }
}

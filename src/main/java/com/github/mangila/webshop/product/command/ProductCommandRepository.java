package com.github.mangila.webshop.product.command;

import com.github.mangila.webshop.product.command.model.ProductDeleteCommand;
import com.github.mangila.webshop.product.command.model.ProductUpsertCommand;
import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.product.model.ProductEntity;
import com.github.mangila.webshop.product.util.ProductRepositoryUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductCommandRepository {

    private final JdbcTemplate jdbc;
    private final ProductRepositoryUtil repositoryUtil;

    public ProductCommandRepository(JdbcTemplate jdbc,
                                    ProductRepositoryUtil repositoryUtil) {
        this.repositoryUtil = repositoryUtil;
        this.jdbc = jdbc;
    }

    public Optional<Product> upsert(ProductUpsertCommand command) {
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
                command.id(),
                command.name(),
                command.price(),
                command.attributes(),
        };
        List<ProductEntity> result = jdbc.query(sql, repositoryUtil.productEntityRowMapper(), params);
        return repositoryUtil.extractOneResult(result);
    }

    public Optional<Product> delete(ProductDeleteCommand command) {
        final String sql = """
                DELETE FROM product WHERE id = ?
                RETURNING id, name, price, created, updated, attributes
                """;
        List<ProductEntity> result = jdbc.query(sql, repositoryUtil.productEntityRowMapper(), command.id());
        return repositoryUtil.extractOneResult(result);
    }
}

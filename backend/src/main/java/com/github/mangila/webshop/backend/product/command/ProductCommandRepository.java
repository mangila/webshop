package com.github.mangila.webshop.backend.product.command;

import com.github.mangila.webshop.backend.common.util.exception.DatabaseException;
import com.github.mangila.webshop.backend.product.command.model.ProductDeleteCommand;
import com.github.mangila.webshop.backend.product.command.model.ProductUpsertCommand;
import com.github.mangila.webshop.backend.product.model.Product;
import com.github.mangila.webshop.backend.product.util.ProductRepositoryUtil;
import io.vavr.control.Try;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
        final Object[] params = new Object[]{
                command.id(),
                command.name(),
                command.price(),
                command.attributes(),
        };
        return Try.of(() -> jdbc.query(sql, repositoryUtil.productEntityRowMapper(), params))
                .map(repositoryUtil::findOne)
                .getOrElseThrow(cause -> new DatabaseException(
                        Product.class,
                        "Upsert failed",
                        sql,
                        params,
                        cause
                ));
    }

    public Optional<Product> delete(ProductDeleteCommand command) {
        final String sql = """
                DELETE FROM product WHERE id = ?
                RETURNING id, name, price, created, updated, attributes
                """;
        final Object[] params = new Object[]{command.id()};
        return Try.of(() -> jdbc.query(sql, repositoryUtil.productEntityRowMapper(), params))
                .map(repositoryUtil::findOne)
                .getOrElseThrow(cause -> new DatabaseException(
                        Product.class,
                        "Delete failed",
                        sql,
                        params,
                        cause
                ));
    }
}

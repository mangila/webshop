package com.github.mangila.webshop.backend.product.infrastructure;

import com.github.mangila.webshop.backend.common.exception.DatabaseException;
import com.github.mangila.webshop.backend.product.domain.command.ProductDeleteCommand;
import com.github.mangila.webshop.backend.product.domain.command.ProductUpsertCommand;
import com.github.mangila.webshop.backend.product.domain.ProductDomain;
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

    public Optional<ProductDomain> upsert(ProductUpsertCommand command) {
        // language=PostgreSQL
        final String sql = """
                INSERT INTO product (id, name, price, attributes)
                VALUES (?, ?, ?, ?::jsonb)
                ON CONFLICT (id)
                DO UPDATE SET name = EXCLUDED.name, price = EXCLUDED.price, attributes = EXCLUDED.attributes
                RETURNING id, name, price, created, updated, attributes
                """;
        final Object[] params = new Object[]{
                command.id(),
                command.name(),
                command.price(),
                command.attributes()
        };
        return Try.of(() -> jdbc.query(sql, repositoryUtil.productEntityRowMapper(), params))
                .map(repositoryUtil::findOne)
                .getOrElseThrow(cause -> new DatabaseException(
                        ProductDomain.class,
                        "Upsert failed",
                        sql,
                        params,
                        cause
                ));
    }

    public Optional<ProductDomain> delete(ProductDeleteCommand command) {
        // language=PostgreSQL
        final String sql = """
                DELETE FROM product WHERE id = ?
                RETURNING id, name, price, created, updated, attributes
                """;
        final Object[] params = new Object[]{command.id()};
        return Try.of(() -> jdbc.query(sql, repositoryUtil.productEntityRowMapper(), params))
                .map(repositoryUtil::findOne)
                .getOrElseThrow(cause -> new DatabaseException(
                        ProductDomain.class,
                        "Delete failed",
                        sql,
                        params,
                        cause
                ));
    }
}

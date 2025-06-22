package com.github.mangila.webshop.backend.product.query;

import com.github.mangila.webshop.backend.common.util.exception.DatabaseException;
import com.github.mangila.webshop.backend.product.model.Product;
import com.github.mangila.webshop.backend.product.query.model.ProductByIdQuery;
import com.github.mangila.webshop.backend.product.util.ProductRepositoryUtil;
import io.vavr.control.Try;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ProductQueryRepository {

    private final JdbcTemplate jdbc;
    private final ProductRepositoryUtil repositoryUtil;

    public ProductQueryRepository(JdbcTemplate jdbc,
                                  ProductRepositoryUtil repositoryUtil) {
        this.repositoryUtil = repositoryUtil;
        this.jdbc = jdbc;
    }

    public Optional<Product> findById(ProductByIdQuery query) {
        final String sql = """
                SELECT id,
                       name,
                       price,
                       created,
                       updated,
                       attributes
                FROM product WHERE id = ?
                """;
        final Object[] params = new Object[]{query.id()};
        return Try.of(() -> jdbc.query(sql, repositoryUtil.productEntityRowMapper(), params))
                .map(repositoryUtil::findOne)
                .getOrElseThrow(cause -> new DatabaseException(
                        Product.class,
                        "Query by id failed",
                        sql,
                        params,
                        cause
                ));
    }
}

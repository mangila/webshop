package com.github.mangila.webshop.product.query;

import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.product.model.ProductEntity;
import com.github.mangila.webshop.product.util.ProductRepositoryUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    public Optional<Product> queryById(String id) {
        final String sql = """
                SELECT id,
                       name,
                       price,
                       created,
                       updated,
                       attributes
                FROM product WHERE id = ?
                """;
        List<ProductEntity> result = jdbc.query(sql, repositoryUtil.productEntityRowMapper(), id);
        return repositoryUtil.extractOneResult(result);
    }
}

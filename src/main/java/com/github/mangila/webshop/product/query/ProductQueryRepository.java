package com.github.mangila.webshop.product.query;

import com.github.mangila.webshop.product.ProductMapper;
import com.github.mangila.webshop.product.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Optional;

@Repository
public class ProductQueryRepository {

    private static final Logger log = LoggerFactory.getLogger(ProductQueryRepository.class);

    private final ProductMapper productMapper;
    private final JdbcTemplate jdbc;

    public ProductQueryRepository(ProductMapper productMapper,
                                  JdbcTemplate jdbc) {
        this.productMapper = productMapper;
        this.jdbc = jdbc;
    }

    public Optional<Product> queryById(String id) {
        final String sql = """
                SELECT id,
                       name,
                       description,
                       price,
                       image_url,
                       category,
                       created,
                       updated,
                       extensions
                FROM product WHERE id = ?
                """;
        log.debug("{} -- {}", id, sql);
        var result = jdbc.query(sql,
                productMapper.getRowMapper(),
                id);
        if (CollectionUtils.isEmpty(result)) {
            return Optional.empty();
        }
        var product = productMapper.toProduct(result.getFirst());
        return Optional.of(product);
    }
}

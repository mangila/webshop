package com.github.mangila.webshop.product;

import com.github.mangila.webshop.product.model.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

    private final JdbcTemplate jdbc;

    public ProductRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Product queryById(String id) {
        return new Product();
    }
}

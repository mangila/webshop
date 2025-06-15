package com.github.mangila.webshop.product;

import com.github.mangila.webshop.product.model.Product;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class ProductRepositoryMapper {

    public RowMapper<Product> getProductRowMapper() {
        return (rs, rowNum) -> {
            var product = new Product();
            product.setId(rs.getString("id"));
            product.setName(rs.getString("name"));
            product.setDescription(rs.getString("description"));
            product.setPrice(rs.getBigDecimal("price"));
            product.setImageUrl(rs.getString("image_url"));
            product.setCategory(rs.getString("category"));
            product.setCreated(rs.getTimestamp("created").toInstant());
            product.setUpdated(rs.getTimestamp("updated").toInstant());
            product.setExtensions(rs.getString("extensions"));
            return product;
        };
    }

}

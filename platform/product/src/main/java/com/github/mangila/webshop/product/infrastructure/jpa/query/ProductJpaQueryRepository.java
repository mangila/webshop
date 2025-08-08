package com.github.mangila.webshop.product.infrastructure.jpa.query;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductQueryRepository;
import com.github.mangila.webshop.product.domain.cqrs.FindProductByStatusQuery;
import com.github.mangila.webshop.product.domain.cqrs.FindProductQuery;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import com.github.mangila.webshop.product.infrastructure.jpa.ProductEntityMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductJpaQueryRepository implements ProductQueryRepository {

    private final ProductEntityMapper mapper;
    private final ProductEntityQueryRepository repository;

    public ProductJpaQueryRepository(ProductEntityMapper mapper,
                                     ProductEntityQueryRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public Optional<Product> findById(FindProductQuery query) {
        return repository.findById(query.id().value())
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsById(ProductId productId) {
        return repository.existsById(productId.value());
    }

    @Override
    public List<Product> findByStatus(FindProductByStatusQuery query) {
        return repository.findByStatus(query.status(), query.limit())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}

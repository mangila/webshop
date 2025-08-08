package com.github.mangila.webshop.product.infrastructure.jpa.query;

import com.github.mangila.webshop.product.domain.types.ProductStatusType;
import com.github.mangila.webshop.product.infrastructure.jpa.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductEntityQueryRepository extends JpaRepository<ProductEntity, UUID> {

    @Query("""
            SELECT p FROM ProductEntity p
            WHERE p.status = :status
            ORDER BY p.created
            LIMIT :limit
            """)
    List<ProductEntity> findByStatus(@Param("status") ProductStatusType status, @Param("limit") int limit);
}

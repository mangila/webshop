package com.github.mangila.webshop.product.infrastructure.jpa.command;

import com.github.mangila.webshop.product.domain.types.ProductStatusType;
import com.github.mangila.webshop.product.infrastructure.jpa.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.UUID;

public interface ProductEntityCommandRepository extends JpaRepository<ProductEntity, UUID> {

    @Modifying(
            clearAutomatically = true,
            flushAutomatically = true)
    @Query("""
            UPDATE ProductEntity p
            SET p.status = :status,
                p.updated = :updated,
                p.version = p.version +1
            WHERE p.id = :id
            """)
    void updateStatus(UUID id,
                      ProductStatusType status,
                      Instant updated);
}

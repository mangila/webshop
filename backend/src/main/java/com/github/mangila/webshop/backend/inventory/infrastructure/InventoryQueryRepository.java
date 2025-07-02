package com.github.mangila.webshop.backend.inventory.infrastructure;

import com.github.mangila.webshop.backend.inventory.domain.model.Inventory;
import com.github.mangila.webshop.backend.inventory.domain.model.InventoryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryQueryRepository extends JpaRepository<Inventory, InventoryId> {
}

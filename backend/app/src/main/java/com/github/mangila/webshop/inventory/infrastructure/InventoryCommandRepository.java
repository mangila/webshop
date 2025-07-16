package com.github.mangila.webshop.inventory.infrastructure;

import com.github.mangila.webshop.inventory.domain.model.Inventory;
import com.github.mangila.webshop.inventory.domain.model.InventoryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryCommandRepository extends JpaRepository<Inventory, InventoryId> {
}

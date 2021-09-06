package net.notfab.lindsey.api.repositories.sql;

import net.notfab.lindsey.shared.entities.items.ItemReference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<ItemReference, Long> {
}

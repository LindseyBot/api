package net.notfab.lindsey.api.repositories.sql;

import net.notfab.lindsey.shared.entities.items.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}

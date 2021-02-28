package net.notfab.lindsey.api.rest;

import net.notfab.lindsey.api.advice.paging.PagedResponse;
import net.notfab.lindsey.api.advice.paging.Paginator;
import net.notfab.lindsey.api.advice.security.SessionProvider;
import net.notfab.lindsey.shared.entities.items.ItemReference;
import net.notfab.lindsey.shared.repositories.sql.InventoryRepository;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("inventory")
public class InventoryRest {

    private final SessionProvider sessionProvider;
    private final InventoryRepository repository;

    public InventoryRest(SessionProvider sessionProvider, InventoryRepository repository) {
        this.sessionProvider = sessionProvider;
        this.repository = repository;
    }

    @GetMapping
    public PagedResponse<ItemReference> findByExample(Paginator paginator, ItemReference reference) {
        reference.setOwner(this.sessionProvider.getUser().getId());
        Example<ItemReference> example = Example.of(reference);
        return new PagedResponse<>(this.repository.findAll(example, paginator.toPageable()));
    }

}

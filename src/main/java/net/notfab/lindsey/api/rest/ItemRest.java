package net.notfab.lindsey.api.rest;

import net.notfab.lindsey.api.advice.paging.PagedResponse;
import net.notfab.lindsey.api.advice.paging.Paginator;
import net.notfab.lindsey.api.repositories.sql.ItemRepository;
import net.notfab.lindsey.shared.entities.items.Item;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("items")
public class ItemRest {

    private final ItemRepository repository;

    public ItemRest(ItemRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public PagedResponse<Item> findByExample(Paginator paginator, Item example) {
        return new PagedResponse<>(repository.findAll(Example.of(example), paginator.toPageable()));
    }

}

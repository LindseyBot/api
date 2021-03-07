package net.notfab.lindsey.api.advice.paging;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

@Getter
public class KeySetPaginator {

    private long last;
    private int limit = 100;

    @Setter
    private String sort = null;

    @Setter
    private String dir = "asc";

    public void setLimit(int limit) {
        if (limit > 100) {
            throw new IllegalArgumentException("Limit cannot be above 100");
        }
        if (limit == 0) {
            return;
        }
        this.limit = limit;
    }

    public Pageable toPageable() {
        if (sort != null) {
            Optional<Sort.Direction> sortDir = Sort.Direction.fromOptionalString(dir);
            if (sortDir.isPresent()) {
                Sort aSort = Sort.by(sortDir.get(), sort);
                return PageRequest.of(0, this.limit, aSort);
            }
        }
        return PageRequest.of(0, this.limit);
    }

    public Pageable toPageable(Sort sort) {
        return PageRequest.of(0, this.limit, sort);
    }

}

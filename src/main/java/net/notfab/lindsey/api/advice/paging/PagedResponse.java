package net.notfab.lindsey.api.advice.paging;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
public class PagedResponse<T> {

    private int page;
    private int limit;
    private boolean last;
    private long total;
    private long totalPages;

    private List<T> items;

    public PagedResponse(Page<T> page) {
        this.page = page.getNumber();
        this.limit = page.getPageable().getPageSize();
        this.last = page.isLast();
        this.items = page.getContent();
        this.total = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }

    public static <T> PagedResponse<T> ofUnpaged(List<T> items) {
        PagedResponse<T> response = new PagedResponse<>();
        response.setItems(items);
        response.setPage(0);
        response.setLast(true);
        response.setLimit(999);
        return response;
    }

}

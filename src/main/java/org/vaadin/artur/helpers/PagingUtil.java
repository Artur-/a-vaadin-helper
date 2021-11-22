package org.vaadin.artur.helpers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.provider.SortOrder;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

public class PagingUtil {

    private PagingUtil() {
        //
    }

    private static PageRequest limitAndOffsetAndSortOrdersToPageSizeAndNumber(int offset, int limit, Sort sort) {
        int minPageSize = limit;
        int lastIndex = offset + limit - 1;
        int maxPageSize = lastIndex + 1;
        for (double pageSize = minPageSize; pageSize <= maxPageSize; pageSize++) {
            int startPage = (int) (offset / pageSize);
            int endPage = (int) (lastIndex / pageSize);
            if (startPage == endPage) {
                // It fits on one page, let's go with that
                return PageRequest.of(startPage, (int) pageSize, sort);
            }
        }

        // Should not really get here
        return PageRequest.of(0, maxPageSize, sort);
    }

    private static Order javaSortOrderToSpringOrder(SortOrder<String> sortOrder) {
        Direction direction = sortOrder.getDirection() == SortDirection.ASCENDING ? Direction.ASC : Direction.DESC;
        return new Order(direction, sortOrder.getSorted());
    }

    private static Sort javaSortToSpring(List<? extends SortOrder<String>> sortOrders) {
        List<Order> orders = sortOrders.stream().map(PagingUtil::javaSortOrderToSpringOrder)
                .collect(Collectors.toList());
        return Sort.by(orders);
    }

    private static Sort tsSortToSpring(List<GridSorter> sortOrders) {
        List<Order> orders = sortOrders.stream().map(PagingUtil::tsSorterToSpringOrder).collect(Collectors.toList());
        return Sort.by(orders);
    }

    private static Order tsSorterToSpringOrder(GridSorter gridSorter) {
        String dir = gridSorter.getDirection().orElse("asc");
        Direction direction = dir.equals("asc") ? Direction.ASC : Direction.DESC;
        return new Order(direction, gridSorter.getPath());
    }

    public static Pageable offsetLimitToPageable(int offset, int limit) {
        return offsetLimitSortOrdersToPageable(offset, limit, Collections.emptyList());
    }

    public static Pageable offsetLimitTypeScriptSortOrdersToPageable(int offset, int limit,
            List<GridSorter> sortOrders) {
        Sort sort = tsSortToSpring(sortOrders);

        PageRequest pageSizeAndNumberAndSortOrders = limitAndOffsetAndSortOrdersToPageSizeAndNumber(offset, limit,
                sort);
        return PageRequest.of(pageSizeAndNumberAndSortOrders.getPageNumber(),
                pageSizeAndNumberAndSortOrders.getPageSize(), pageSizeAndNumberAndSortOrders.getSort());
    }

    public static Pageable offsetLimitSortOrdersToPageable(int offset, int limit, List<? extends SortOrder<String>> sortOrders) {
        Sort sort = javaSortToSpring(sortOrders);
        PageRequest pageSizeAndNumberAndSortOrders = limitAndOffsetAndSortOrdersToPageSizeAndNumber(offset, limit,
                sort);

        return PageRequest.of(pageSizeAndNumberAndSortOrders.getPageNumber(),
                pageSizeAndNumberAndSortOrders.getPageSize(), pageSizeAndNumberAndSortOrders.getSort());
    }

}

package org.vaadin.artur.helpers;

import java.util.Collection;
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

    private static PageRequest limitAndOffsetAndSortOrdersToPageSizeAndNumber(int offset, int limit, List<SortOrder<String>> sortOrders) {
        int minPageSize = limit;
        int lastIndex = offset + limit - 1;
        int maxPageSize = lastIndex + 1;
        List<Order> orders =  sortOrders.stream().map(PagingUtil::vaadinSortOrderToSpringOrder).collect(Collectors.toList());
        Sort sort = Sort.by(orders);
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

    private static Order vaadinSortOrderToSpringOrder(SortOrder<String> sortOrder){
        Direction direction = sortOrder.getDirection() == SortDirection.ASCENDING ? Direction.ASC : Direction.DESC;
        return new Order(direction, sortOrder.getSorted());
    }

    public static Pageable offsetLimitToPageable(int offset, int limit) {
        return offsetLimitSortOrdersToPageable(offset, limit, Collections.emptyList());
    }

    public static Pageable offsetLimitSortOrdersToPageable(int offset, int limit, List<SortOrder<String>> sortOrders) {
        PageRequest pageSizeAndNumberAndSortOrders = limitAndOffsetAndSortOrdersToPageSizeAndNumber(offset, limit, sortOrders);
        return PageRequest.of(pageSizeAndNumberAndSortOrders.getPageNumber(), pageSizeAndNumberAndSortOrders.getPageSize(), pageSizeAndNumberAndSortOrders.getSort());
    }

}

package org.vaadin.artur.helpers;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PagingUtil {

    private PagingUtil() {
        //
    }

    private static PageRequest limitAndOffsetToPageSizeAndNumber(int offset, int limit) {
        int minPageSize = limit;
        int lastIndex = offset + limit - 1;
        int maxPageSize = lastIndex + 1;
        for (double pageSize = minPageSize; pageSize <= maxPageSize; pageSize++) {
            int startPage = (int) (offset / pageSize);
            int endPage = (int) (lastIndex / pageSize);
            if (startPage == endPage) {
                // It fits on one page, let's go with that
                return PageRequest.of(startPage, (int) pageSize);
            }
        }
        // Should not really get here
        return PageRequest.of(0, maxPageSize);
    }

    public static Pageable offsetLimitToPageable(int offset, int limit) {
        PageRequest pageSizeAndNumber = limitAndOffsetToPageSizeAndNumber(offset, limit);
        return PageRequest.of(pageSizeAndNumber.getPageNumber(), pageSizeAndNumber.getPageSize());
    }

}

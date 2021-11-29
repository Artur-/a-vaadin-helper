package org.vaadin.artur.helpers;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.provider.SortOrder;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.domain.Pageable;

public class PagingUtilTest {

    @Test
    public void worksWithSortOrder() {
        List<SortOrder<String>> sortOrders = new ArrayList<>();
        sortOrders.add(new SortOrder<String>("foo", SortDirection.ASCENDING));
        Pageable pageable = PagingUtil.offsetLimitSortOrdersToPageable(100, 20, sortOrders);

        Assert.assertEquals(100L, pageable.getOffset());
        Assert.assertEquals(20, pageable.getPageSize());
        Assert.assertEquals(5, pageable.getPageNumber());
        Assert.assertTrue(pageable.getSort().getOrderFor("foo").isAscending());
    }

    @Test
    public void worksWithSortOrderSubClass() {
        List<QuerySortOrder> sortOrders = new ArrayList<>();
        sortOrders.add(new QuerySortOrder("foo", SortDirection.DESCENDING));
        Pageable pageable = PagingUtil.offsetLimitSortOrdersToPageable(100, 20, sortOrders);

        Assert.assertEquals(100L, pageable.getOffset());
        Assert.assertEquals(20, pageable.getPageSize());
        Assert.assertEquals(5, pageable.getPageNumber());
        Assert.assertTrue(pageable.getSort().getOrderFor("foo").isDescending());
    }
}

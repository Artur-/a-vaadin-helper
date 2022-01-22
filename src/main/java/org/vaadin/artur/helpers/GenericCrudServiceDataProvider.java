package org.vaadin.artur.helpers;

import java.util.Arrays;
import java.util.List;

import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.vaadin.artur.spring.dataprovider.FilterablePageableDataProvider;

public class GenericCrudServiceDataProvider<T, F, IDTYPE> extends FilterablePageableDataProvider<T, F> {

    private final CrudService<T, IDTYPE> service;
    private List<QuerySortOrder> defaultSortOrders;

    public GenericCrudServiceDataProvider(CrudService<T, IDTYPE> service, QuerySortOrder... defaultSortOrders) {
        this.service = service;
        this.defaultSortOrders = Arrays.asList(defaultSortOrders);
    }

    @Override
    protected Page<T> fetchFromBackEnd(Query<T, F> query, Pageable pageable) {
        return service.list(pageable);
    }

    @Override
    protected List<QuerySortOrder> getDefaultSortOrders() {
        return defaultSortOrders;
    }

    @Override
    protected int sizeInBackEnd(Query<T, F> query) {
        return service.count();
    }
}

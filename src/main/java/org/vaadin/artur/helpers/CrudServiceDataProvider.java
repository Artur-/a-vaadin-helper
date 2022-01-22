package org.vaadin.artur.helpers;

import com.vaadin.flow.data.provider.QuerySortOrder;

public class CrudServiceDataProvider<T, F> extends GenericCrudServiceDataProvider<T, F, Integer> {
    public CrudServiceDataProvider(CrudService<T, Integer> service, QuerySortOrder... defaultSortOrders) {
        super(service, defaultSortOrders);
    }

}

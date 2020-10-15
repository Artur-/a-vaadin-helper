package org.vaadin.artur.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.provider.SortOrder;

public abstract class CrudService<T, ID> {

    public static class ListData<T> {
        public List<T> data;
        public int count;

        public ListData(List<T> data, int count) {
            this.data = data;
            this.count = count;
        }
    }

    public static class ListParams {
        public static class ListOrder {
            public String direction;
            public String path;
        }

        public int page;
        public int pageSize;
        public List<ListOrder> sortOrders;
    }

    protected abstract JpaRepository<T, ID> getRepository();

    public Optional<T> get(ID id) {
        return getRepository().findById(id);
    }

    public T update(T entity) {
        return getRepository().save(entity);
    }

    public void delete(ID id) {
        getRepository().deleteById(id);
    }

    public Page<T> list(Pageable pageable) {
        return getRepository().findAll(pageable);
    }

    public ListData<T> list(ListParams params) {
        ArrayList<SortOrder<String>> sorts = new ArrayList<>();
        for (ListParams.ListOrder sort : params.sortOrders) {
            sorts.add(new SortOrder<String>(sort.path,
                    "asc".equals(sort.direction)
                            ? SortDirection.ASCENDING
                            : SortDirection.DESCENDING));
        }
        Pageable pageable = PagingUtil.offsetLimitSortOrdersToPageable(
                params.page, params.pageSize, sorts);
        return new ListData<T>(list(pageable).getContent(), count());
    }

    public int count() {
        return (int)getRepository().count();
    }

}

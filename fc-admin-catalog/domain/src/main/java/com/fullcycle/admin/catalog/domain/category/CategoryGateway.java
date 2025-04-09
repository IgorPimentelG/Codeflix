package com.fullcycle.admin.catalog.domain.category;

import com.fullcycle.admin.catalog.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalog.domain.pagination.Pagination;

import java.util.List;
import java.util.Optional;

public interface CategoryGateway {
    Category create(Category category);
    Category update(Category category);
    Optional<Category> findById(CategoryID id);
    List<CategoryID> existsByIds(Iterable<CategoryID> id);
    Pagination<Category> findAll(SearchQuery query);
    void delete(CategoryID id);
}

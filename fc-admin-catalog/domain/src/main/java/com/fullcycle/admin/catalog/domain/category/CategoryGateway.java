package com.fullcycle.admin.catalog.domain.category;

import com.fullcycle.admin.catalog.domain.pagination.Pagination;

import java.util.Optional;

public interface CategoryGateway {
    Category create(Category category);
    Category update(Category category);
    Optional<Category> findById(CategoryID id);
    Pagination<Category> findAll(CategorySearchQuery query);
    void delete(CategoryID id);
}

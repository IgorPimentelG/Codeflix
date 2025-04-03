package com.fullcycle.admin.catalog.domain.genre;

import com.fullcycle.admin.catalog.domain.pagination.Pagination;
import com.fullcycle.admin.catalog.domain.pagination.SearchQuery;

import java.util.Optional;

public interface GenreGateway {
    Genre create(Genre genre);
    Genre update(Genre genre);
    Optional<Genre> findById(GenreID id);
    Pagination<Genre> finalAll(SearchQuery query);
    void deleteById(GenreID id);
}

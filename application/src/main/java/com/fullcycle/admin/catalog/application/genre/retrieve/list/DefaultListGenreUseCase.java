package com.fullcycle.admin.catalog.application.genre.retrieve.list;

import com.fullcycle.admin.catalog.domain.genre.GenreGateway;
import com.fullcycle.admin.catalog.domain.pagination.Pagination;
import com.fullcycle.admin.catalog.domain.pagination.SearchQuery;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultListGenreUseCase extends ListGenreUseCase {

    private final GenreGateway genreGateway;

    @Override
    public Pagination<GenreListOutput> execute(final SearchQuery query) {
        return genreGateway.finalAll(query).map(GenreListOutput::from);
    }
}

package com.fullcycle.admin.catalog.application.genre.retrieve.list;

import com.fullcycle.admin.catalog.IntegrationTest;
import com.fullcycle.admin.catalog.domain.genre.Genre;
import com.fullcycle.admin.catalog.domain.genre.GenreGateway;
import com.fullcycle.admin.catalog.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalog.infrastructure.genre.persistence.GenreJpaEntity;
import com.fullcycle.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
public class ListGenreUseCaseIT {

    @Autowired
    private ListGenreUseCase useCase;

    @Autowired
    private GenreGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void givenValidQuery_whenCallsListGenre_shouldReturnGenres() {
        final var genres = List.of(Genre.newGenre("any name 1", true), Genre.newGenre("any name 2", true));
        genreRepository.saveAllAndFlush(genres.stream().map(GenreJpaEntity::from).toList());

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "any";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = genres.stream()
          .map(GenreListOutput::from)
          .toList();

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var output = useCase.execute(query);

        assertEquals(expectedPage, output.currentPage());
        assertEquals(expectedPerPage, output.perPage());
        assertEquals(expectedTotal, output.total());
        assertEquals(expectedItems, output.items());
    }
}

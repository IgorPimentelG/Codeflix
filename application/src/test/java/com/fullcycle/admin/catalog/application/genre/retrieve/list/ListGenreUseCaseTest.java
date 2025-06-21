package com.fullcycle.admin.catalog.application.genre.retrieve.list;

import com.fullcycle.admin.catalog.application.UseCaseTest;
import com.fullcycle.admin.catalog.domain.genre.Genre;
import com.fullcycle.admin.catalog.domain.genre.GenreGateway;
import com.fullcycle.admin.catalog.domain.pagination.Pagination;
import com.fullcycle.admin.catalog.domain.pagination.SearchQuery;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ListGenreUseCaseTest extends UseCaseTest {

    @Mock
    private GenreGateway genreGateway;

    @InjectMocks
    private DefaultListGenreUseCase useCase;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void givenValidQuery_whenCallsListGenre_shouldReturnGenres() {
        final var genres = List.of(Genre.newGenre("any name 1", true), Genre.newGenre("any name 2", true));
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "any";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = genres.stream()
          .map(GenreListOutput::from)
          .toList();

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, expectedTotal, genres);

        when(genreGateway.finalAll(any())).thenReturn(expectedPagination);

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var output = useCase.execute(query);

        assertEquals(expectedPage, output.currentPage());
        assertEquals(expectedPerPage, output.perPage());
        assertEquals(expectedTotal, output.total());
        assertEquals(expectedItems, output.items());
        verify(genreGateway, times(1)).finalAll(query);
    }

    @Test
    public void givenValidQuery_whenCallsListGenreAndResultIsEmpty_shouldReturnGenres() {
        final var genres = List.<Genre>of();
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "any";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var expectedItems = genres.stream()
          .map(GenreListOutput::from)
          .toList();

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, expectedTotal, genres);

        when(genreGateway.finalAll(any())).thenReturn(expectedPagination);

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var output = useCase.execute(query);

        assertEquals(expectedPage, output.currentPage());
        assertEquals(expectedPerPage, output.perPage());
        assertEquals(expectedTotal, output.total());
        assertEquals(expectedItems, output.items());
        verify(genreGateway, times(1)).finalAll(query);
    }

    @Test
    public void givenValidQuery_whenCallsListGenreAndGatewayThrowsRandomError_shouldReturnException() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "any";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "Gateway error";

        when(genreGateway.finalAll(any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var output = assertThrows(IllegalStateException.class, () -> useCase.execute(query));

        assertEquals(expectedErrorMessage, output.getMessage());
    }
}

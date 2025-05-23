package com.fullcycle.admin.catalog.application.category.retrieve.list;

import com.fullcycle.admin.catalog.application.UseCaseTest;
import com.fullcycle.admin.catalog.domain.category.Category;
import com.fullcycle.admin.catalog.domain.category.CategoryGateway;
import com.fullcycle.admin.catalog.domain.pagination.Pagination;
import com.fullcycle.admin.catalog.domain.pagination.SearchQuery;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


public class ListCategoriesUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultListCategoriesUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway);
    }

    @Test
    public void givenValidQuery_whenCallsListCategories_thenShouldReturnCategoriesList() {
        final var categories = List.of(
          Category.newCategory("Any category 1", null, true),
          Category.newCategory("Any category 2", null, true)
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);

        final var expectedItemsCounts = 2;
        final var expectedResult = expectedPagination.map(CategoryListOutput::from);

        when(categoryGateway.findAll(eq(query))).thenReturn(expectedPagination);

        final var output = useCase.execute(query);

        assertEquals(expectedItemsCounts, output.items().size());
        assertEquals(expectedResult, output);
        assertEquals(expectedPage, output.currentPage());
        assertEquals(expectedPerPage, output.perPage());
        assertEquals(categories.size(), output.total());
    }

    @Test
    public void givenValidQuery_whenHasNoResults_thenShouldReturnEmptyCategoriesList() {
        final var categories = List.<Category>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);

        final var expectedItemsCounts = 0;
        final var expectedResult = expectedPagination.map(CategoryListOutput::from);

        when(categoryGateway.findAll(eq(query))).thenReturn(expectedPagination);

        final var output = useCase.execute(query);

        assertEquals(expectedItemsCounts, output.items().size());
        assertEquals(expectedResult, output);
        assertEquals(expectedPage, output.currentPage());
        assertEquals(expectedPerPage, output.perPage());
        assertEquals(categories.size(), output.total());
    }

    @Test
    public void givenValidQuery_whenGatewayThrowsException_shouldReturnException() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "Gateway error";

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        when(categoryGateway.findAll(eq(query))).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var exception = assertThrows(IllegalStateException.class, () -> useCase.execute(query));

        assertEquals(expectedErrorMessage, exception.getMessage());
    }


}

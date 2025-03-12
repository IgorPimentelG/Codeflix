package com.fullcycle.admin.catalog.application.category.retrieve.list;

import com.fullcycle.admin.catalog.IntegrationTest;
import com.fullcycle.admin.catalog.domain.category.Category;
import com.fullcycle.admin.catalog.domain.category.CategoryGateway;
import com.fullcycle.admin.catalog.domain.category.CategorySearchQuery;
import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
public class ListCategoriesIT {

    @Autowired
    private ListCategoriesUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @MockitoSpyBean
    private CategoryGateway categoryGateway;

    @BeforeEach
    void mockUp() {
        final var categories = Stream.of(
              Category.newCategory("Filmes", null, true),
              Category.newCategory("Netflix Originals", "Títulos de autoria da Netflix", true),
              Category.newCategory("Amazon Originals", "Títulos de autoria da Amazon", true),
              Category.newCategory("Documentários", null, true),
              Category.newCategory("Sports", null, true),
              Category.newCategory("Kids", "Categorias para crianças", true),
              Category.newCategory("Series", null, true)
        ).map(CategoryJpaEntity::from).toList();

        categoryRepository.saveAllAndFlush(categories);
    }

    @Test
    public void givenValidTerm_whenTermDoesntMatchsPrePersisted_shouldReturnEmptyPage() {
            final var expectedPage = 0;
            final var expectedPerPage = 10;
            final var expectedTerms = "any-terms";
            final var expectedSort = "name";
            final var expectedDirection = "asc";
            final var expectedItemsCount = 0;
            final var expectedTotal = 0;

            final var query = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

            final var output = useCase.execute(query);

            assertEquals(expectedItemsCount, output.items().size());
            assertEquals(expectedPage, output.currentPage());
            assertEquals(expectedPerPage, output.perPage());
            assertEquals(expectedTotal, output.total());
      }

    @ParameterizedTest
    @CsvSource({
        "fil,0,10,1,1,Filmes",
        "net,0,10,1,1,Netflix Originals",
        "ZON,0,10,1,1,Amazon Originals",
        "KI,0,10,1,1,Kids",
        "crianças,0,10,1,1,Kids",
        "da Amazon,0,10,1,1,Amazon Originals"
    })
    public void givenValidTerm_whenCallsListCategories_shouldReturnCategoriesFiltered(
      final String expectedTerms,
      final int expectedPage,
      final int expectedPerPage,
      final int expectedItemsCount,
      final long expectedTotal,
      final String expectedCategoryName
    ) {
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var query = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var output = useCase.execute(query);

        assertEquals(expectedItemsCount, output.items().size());
        assertEquals(expectedPage, output.currentPage());
        assertEquals(expectedPerPage, output.perPage());
        assertEquals(expectedTotal, output.total());
        assertEquals(expectedCategoryName, output.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
        "name,asc,0,10,7,7,Amazon Originals",
        "name,desc,0,10,7,7,Sports",
        "createdAt,asc,0,10,7,7,Filmes",
        "createdAt,desc,0,10,7,7,Series"
    })
    public void givenValidSortAndDirection_whenCallsListCategories_thenShouldReturnCategories(
      final String expectedSort,
      final String expectedDirection,
      final int expectedPage,
      final int expectedPerPage,
      final int expectedItemsCount,
      final long expectedTotal
    ) {
        final var expectedTerms = "";

        final var query = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var output = useCase.execute(query);

        assertEquals(expectedItemsCount, output.items().size());
        assertEquals(expectedPage, output.currentPage());
        assertEquals(expectedPerPage, output.perPage());
        assertEquals(expectedTotal, output.total());
    }

    @ParameterizedTest
    @CsvSource({
        "0,2,2,7,Amazon Originals;Documentários",
        "1,2,2,7,Filmes;Kids",
        "2,2,2,7,Netflix Originals;Series",
        "3,2,1,7,Sports"
    })
    public void givenValidPage_whenCallsListCategories_shouldReturnCategoriesPaginated(
      final int expectedPage,
      final int expectedPerPage,
      final int expectedItemsCount,
      final long expectedTotal,
      final String expectedCategoriesName
    ) {
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTerms = "";

        final var query = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var output = useCase.execute(query);

        assertEquals(expectedItemsCount, output.items().size());
        assertEquals(expectedPage, output.currentPage());
        assertEquals(expectedPerPage, output.perPage());
        assertEquals(expectedTotal, output.total());

        int index = 0;
        for (String expectedName : expectedCategoriesName.split(";")) {
            assertEquals(expectedName, output.items().get(index).name());
            index++;
        }
    }
}

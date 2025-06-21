package com.fullcycle.admin.catalog.infrastructure.genre;

import com.fullcycle.admin.catalog.MySQLGatewayTest;
import com.fullcycle.admin.catalog.domain.category.Category;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.genre.Genre;
import com.fullcycle.admin.catalog.domain.genre.GenreID;
import com.fullcycle.admin.catalog.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalog.infrastructure.category.CategoryMySQLGateway;
import com.fullcycle.admin.catalog.infrastructure.genre.persistence.GenreCategoryJpaEntity;
import com.fullcycle.admin.catalog.infrastructure.genre.persistence.GenreJpaEntity;
import com.fullcycle.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@MySQLGatewayTest
public class GenreMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryGateway;

    @Autowired
    private GenreMySQLGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void givenValidGenre_whenCallsCreateGenre_shouldPersistGenre() {
        final var category = Category.newCategory("any name", null, true);
        categoryGateway.create(category);

        final var expectedName = "any name";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(category.getId());

        final var genre = Genre.newGenre(expectedName, expectedIsActive);
        genre.addCategories(expectedCategories);

        assertEquals(0, genreRepository.count());

        final var output = genreGateway.create(genre);

        assertEquals(1, genreRepository.count());
        assertEquals(genre.getId(), output.getId());
        assertEquals(expectedName, output.getName());
        assertEquals(expectedIsActive, output.isActive());
        assertEquals(expectedCategories, output.getCategories());
        assertEquals(genre.getCreatedAt(), output.getCreatedAt());
        assertEquals(genre.getUpdatedAt(), output.getUpdatedAt());
        assertEquals(genre.getDeletedAt(), output.getDeletedAt());

        final var persisted = genreRepository.findById(genre.getId().getValue()).get();

        assertEquals(expectedName, persisted.getName());
        assertEquals(expectedIsActive, persisted.isActive());
        assertEquals(expectedCategories, toCategoryId(persisted.getCategories()));
        assertEquals(genre.getCreatedAt(), persisted.getCreatedAt());
        assertEquals(genre.getUpdatedAt(), persisted.getUpdatedAt());
        assertEquals(genre.getDeletedAt(), persisted.getDeletedAt());
    }

    @Test
    public void givenValidGenreWithoutCategories_whenCallsCreateGenre_shouldPersistGenre() {
        final var expectedName = "any name";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var genre = Genre.newGenre(expectedName, expectedIsActive);

        assertEquals(0, genreRepository.count());

        final var output = genreGateway.create(genre);

        assertEquals(1, genreRepository.count());
        assertEquals(genre.getId(), output.getId());
        assertEquals(expectedName, output.getName());
        assertEquals(expectedIsActive, output.isActive());
        assertEquals(expectedCategories, output.getCategories());
        assertEquals(genre.getCreatedAt(), output.getCreatedAt());
        assertEquals(genre.getUpdatedAt(), output.getUpdatedAt());
        assertEquals(genre.getDeletedAt(), output.getDeletedAt());

        final var persisted = genreRepository.findById(genre.getId().getValue()).get();

        assertEquals(expectedName, persisted.getName());
        assertEquals(expectedIsActive, persisted.isActive());
        assertEquals(expectedCategories, toCategoryId(persisted.getCategories()));
        assertEquals(genre.getCreatedAt(), persisted.getCreatedAt());
        assertEquals(genre.getUpdatedAt(), persisted.getUpdatedAt());
        assertEquals(genre.getDeletedAt(), persisted.getDeletedAt());
    }

    @Test
    public void givenValidGenreWithoutCategories_whenCallsUpdateGenreWithCategories_shouldPersistGenre() {
        final var category1 = Category.newCategory("any name 1", null, true);
        categoryGateway.create(category1);

        final var category2 = Category.newCategory("any name 2", null, true);
        categoryGateway.create(category2);

        final var expectedName = "any name updated";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var genre = Genre.newGenre("any name", expectedIsActive);

        assertEquals(0, genreRepository.count());
        genreRepository.saveAndFlush(GenreJpaEntity.from(genre));
        assertEquals(1, genreRepository.count());

        final var output = genreGateway.update(
          Genre.with(genre).update(expectedName, expectedIsActive, expectedCategories)
        );

        assertEquals(genre.getId(), output.getId());
        assertEquals(expectedName, output.getName());
        assertEquals(expectedIsActive, output.isActive());
        assertEquals(expectedCategories, output.getCategories());
        assertEquals(genre.getCreatedAt(), output.getCreatedAt());
        assertNotNull(output.getUpdatedAt());
        assertEquals(genre.getDeletedAt(), output.getDeletedAt());

        final var persisted = genreRepository.findById(genre.getId().getValue()).get();

        assertEquals(expectedName, persisted.getName());
        assertEquals(expectedIsActive, persisted.isActive());
        assertEquals(expectedCategories, toCategoryId(persisted.getCategories()));
        assertEquals(genre.getCreatedAt(), persisted.getCreatedAt());
        assertNotNull(persisted.getUpdatedAt());
        assertEquals(genre.getDeletedAt(), persisted.getDeletedAt());
    }

    @Test
    public void givenValidGenreWithoutCategories_whenCallsUpdateGenreCleaningCategories_shouldPersistGenre() {
        final var category1 = Category.newCategory("any name 1", null, true);
        categoryGateway.create(category1);

        final var category2 = Category.newCategory("any name 2", null, true);
        categoryGateway.create(category2);

        final var expectedName = "any name updated";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var genre = Genre.newGenre("any name", expectedIsActive);
        genre.addCategories(List.of(category1.getId(), category2.getId()));

        assertEquals(0, genreRepository.count());
        genreRepository.saveAndFlush(GenreJpaEntity.from(genre));
        assertEquals(1, genreRepository.count());

        final var output = genreGateway.update(
          Genre.with(genre).update(expectedName, expectedIsActive, expectedCategories)
        );

        assertEquals(genre.getId(), output.getId());
        assertEquals(expectedName, output.getName());
        assertEquals(expectedIsActive, output.isActive());
        assertEquals(expectedCategories, output.getCategories());
        assertEquals(genre.getCreatedAt(), output.getCreatedAt());
        assertNotNull(output.getUpdatedAt());
        assertEquals(genre.getDeletedAt(), output.getDeletedAt());

        final var persisted = genreRepository.findById(genre.getId().getValue()).get();

        assertEquals(expectedName, persisted.getName());
        assertEquals(expectedIsActive, persisted.isActive());
        assertEquals(expectedCategories, toCategoryId(persisted.getCategories()));
        assertEquals(genre.getCreatedAt(), persisted.getCreatedAt());
        assertNotNull(persisted.getUpdatedAt());
        assertEquals(genre.getDeletedAt(), persisted.getDeletedAt());
    }

    @Test
    public void givenValidGenreInactive_whenCallsUpdateGenreActivating_shouldPersistGenre() {
        final var expectedName = "any name updated";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var genre = Genre.newGenre("any name", false);

        assertEquals(0, genreRepository.count());
        genreRepository.saveAndFlush(GenreJpaEntity.from(genre));
        assertEquals(1, genreRepository.count());

        assertFalse(genre.isActive());
        assertNotNull(genre.getDeletedAt());

        final var output = genreGateway.update(
          Genre.with(genre).update(expectedName, expectedIsActive, expectedCategories)
        );

        assertEquals(genre.getId(), output.getId());
        assertEquals(expectedName, output.getName());
        assertEquals(expectedIsActive, output.isActive());
        assertEquals(expectedCategories, output.getCategories());
        assertEquals(genre.getCreatedAt(), output.getCreatedAt());
        assertNotNull(output.getUpdatedAt());
        assertNull(output.getDeletedAt());

        final var persisted = genreRepository.findById(genre.getId().getValue()).get();

        assertEquals(expectedName, persisted.getName());
        assertEquals(expectedIsActive, persisted.isActive());
        assertEquals(expectedCategories, toCategoryId(persisted.getCategories()));
        assertEquals(genre.getCreatedAt(), persisted.getCreatedAt());
        assertNotNull(persisted.getUpdatedAt());
        assertNull(output.getDeletedAt());
    }

    @Test
    public void givenValidGenreActive_whenCallsUpdateGenreInactivating_shouldPersistGenre() {
        final var expectedName = "any name updated";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var genre = Genre.newGenre("any name", true);

        assertEquals(0, genreRepository.count());
        genreRepository.saveAndFlush(GenreJpaEntity.from(genre));
        assertEquals(1, genreRepository.count());

        assertTrue(genre.isActive());
        assertNull(genre.getDeletedAt());

        final var output = genreGateway.update(
          Genre.with(genre).update(expectedName, expectedIsActive, expectedCategories)
        );

        assertEquals(genre.getId(), output.getId());
        assertEquals(expectedName, output.getName());
        assertEquals(expectedIsActive, output.isActive());
        assertEquals(expectedCategories, output.getCategories());
        assertEquals(genre.getCreatedAt(), output.getCreatedAt());
        assertNotNull(output.getUpdatedAt());
        assertNotNull(output.getDeletedAt());

        final var persisted = genreRepository.findById(genre.getId().getValue()).get();

        assertEquals(expectedName, persisted.getName());
        assertEquals(expectedIsActive, persisted.isActive());
        assertEquals(expectedCategories, toCategoryId(persisted.getCategories()));
        assertEquals(genre.getCreatedAt(), persisted.getCreatedAt());
        assertNotNull(persisted.getUpdatedAt());
        assertNotNull(output.getDeletedAt());
    }

    @Test
    public void givenValidGenre_whenCallsDeleteById_shouldDeleteGenre() {
        final var genre = Genre.newGenre("any name", true);

        assertEquals(0, genreRepository.count());
        genreRepository.saveAndFlush(GenreJpaEntity.from(genre));
        assertEquals(1, genreRepository.count());

        genreGateway.deleteById(genre.getId());
        assertEquals(0, genreRepository.count());
    }

    @Test
    public void givenPrePersistedGenre_whenCallsFindById_shouldReturnGenre() {
        final var category1 = Category.newCategory("any name 1", null, true);
        categoryGateway.create(category1);

        final var category2 = Category.newCategory("any name 2", null, true);
        categoryGateway.create(category2);

        final var expectedName = "any name";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var genre = Genre.newGenre(expectedName, expectedIsActive);

        assertEquals(0, genreRepository.count());
        genreRepository.saveAndFlush(GenreJpaEntity.from(genre));
        assertEquals(1, genreRepository.count());

        final var output = genreGateway.findById(genre.getId()).get();

        assertEquals(1, genreRepository.count());
        assertEquals(genre.getId(), output.getId());
        assertEquals(expectedName, output.getName());
        assertEquals(expectedIsActive, output.isActive());
        assertEquals(expectedCategories, output.getCategories());
        assertEquals(genre.getCreatedAt(), output.getCreatedAt());
        assertEquals(genre.getUpdatedAt(), output.getUpdatedAt());
        assertEquals(genre.getDeletedAt(), output.getDeletedAt());
    }

    @Test
    public void givenInvalidGenreId_whenCallsFindById_shouldReturnGenreEmpty() {
       final var expectedId = GenreID.from(UUID.randomUUID());

        assertEquals(0, genreRepository.count());

        final var output = genreGateway.findById(expectedId);

        assertTrue(output.isEmpty());
    }

    @Test
    public void givenEmptyGenres_whenCallsFindAll_shouldReturnEmptyList() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;
        final var expectedDirection = "asc";
        final var expectedSort = "name";
        final var expectedTerms = "";

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var output = genreGateway.finalAll(query);

        assertEquals(expectedPage, output.currentPage());
        assertEquals(expectedPerPage, output.perPage());
        assertEquals(expectedTotal, output.total());
        assertEquals(expectedTotal, output.items().size());
    }

    @ParameterizedTest
    @CsvSource({
        "aç,0,10,1,1,Ação",
        "dr,0,10,1,1,Drama",
        "com,0,10,1,1,Comédia romântica",
        "cien,0,10,1,1,Ficção científica",
        "terr,0,10,1,1,Terror",
    })
    public void givenValidTerm_whenCallsFindAll_shouldReturnFiltered(
      final String expectedTerms ,
      final int expectedPage,
      final int expectedPerPage,
      final int expectedItemsCount,
      final long expectedTotal,
      final String expectedGenreName
    ) {
        mockGenres();
        final var expectedDirection = "asc";
        final var expectedSort = "name";

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var output = genreGateway.finalAll(query);

        assertEquals(expectedPage, output.currentPage());
        assertEquals(expectedPerPage, output.perPage());
        assertEquals(expectedTotal, output.total());
        assertEquals(expectedItemsCount, output.items().size());
        assertEquals(expectedGenreName, output.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
      "name,asc,0,10,5,5,Ação",
      "name,desc,0,10,5,5,Terror",
      "createdAt,asc,0,10,5,5,Comédia romântica",
    })
    public void givenValidSortAndDirection_whenCallsFindAll_shouldReturnFiltered(
      final String expectedSort ,
      final String expectedDirection,
      final int expectedPage,
      final int expectedPerPage,
      final int expectedItemsCount,
      final long expectedTotal,
      final String expectedGenreName
    ) {
        mockGenres();
        final var expectedTerms = "";

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var output = genreGateway.finalAll(query);

        assertEquals(expectedPage, output.currentPage());
        assertEquals(expectedPerPage, output.perPage());
        assertEquals(expectedTotal, output.total());
        assertEquals(expectedItemsCount, output.items().size());
        assertEquals(expectedGenreName, output.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
      "0,2,2,5,Ação;Comédia romântica",
      "1,2,2,5,Drama;Ficção científica",
      "2,2,1,5,Terror",
    })
    public void givenValidSortAndDirection_whenCallsFindAll_shouldReturnFiltered(
      final int expectedPage,
      final int expectedPerPage,
      final int expectedItemsCount,
      final long expectedTotal,
      final String expectedGenres
    ) {
        mockGenres();
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var output = genreGateway.finalAll(query);

        assertEquals(expectedPage, output.currentPage());
        assertEquals(expectedPerPage, output.perPage());
        assertEquals(expectedTotal, output.total());
        assertEquals(expectedItemsCount, output.items().size());

        int index = 0;
        for (String expectedName: expectedGenres.split(";")) {
            final var name = output.items().get(index).getName();
            assertEquals(expectedName, name);
            index++;
        }
    }

    private void mockGenres() {
        genreRepository.saveAllAndFlush(List.of(
          GenreJpaEntity.from(Genre.newGenre("Comédia romântica", true)),
          GenreJpaEntity.from(Genre.newGenre("Ação", true)),
          GenreJpaEntity.from(Genre.newGenre("Drama", true)),
          GenreJpaEntity.from(Genre.newGenre("Terror", true)),
          GenreJpaEntity.from(Genre.newGenre("Ficção científica", true))
        ));
    }

    private List<CategoryID> toCategoryId(final Set<GenreCategoryJpaEntity> categories) {
        return categories.stream()
          .map(category -> CategoryID.from(category.getId().getCategoryId()))
          .toList();
    }
}

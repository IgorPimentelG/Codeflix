package com.fullcycle.admin.catalog.application.genre.update;

import com.fullcycle.admin.catalog.IntegrationTest;
import com.fullcycle.admin.catalog.domain.category.Category;
import com.fullcycle.admin.catalog.domain.category.CategoryGateway;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.errors.NotificationException;
import com.fullcycle.admin.catalog.domain.genre.Genre;
import com.fullcycle.admin.catalog.domain.genre.GenreGateway;
import com.fullcycle.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
public class UpdateGenreUseCaseIT {

    @Autowired
    private UpdateGenreUseCase useCase;

    @Autowired
    private GenreGateway genreGateway;

    @Autowired
    private CategoryGateway categoryGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void givenValidCommand_whenCallsUpdateGenre_shouldReturnGenreId() {
        final var genre = Genre.newGenre("any name", true);
        final var expectedId = genre.getId();
        genreGateway.create(genre);

        final var expectedName = "any name updated";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var command = UpdateGenreCommand.with(
          expectedId.getValue(),
          expectedName,
          expectedIsActive,
          asString(expectedCategories)
        );

        final var output = useCase.execute(command);

        assertNotNull(output);
        assertEquals(expectedId.getValue(), output.id());

        final var persisted = genreRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, persisted.getName());
        assertEquals(expectedIsActive, persisted.isActive());
        assertEquals(expectedCategories.size(), persisted.getCategories().size());
        assertNotNull(persisted.getCreatedAt());
        assertNotNull(persisted.getUpdatedAt());
        assertNull(persisted.getDeletedAt());
    }

    @Test
    public void givenValidCommandWithInactiveGenre_whenCallsUpdateGenre_shouldReturnGenreId() {
        final var genre = Genre.newGenre("any name", true);
        final var expectedId = genre.getId();
        genreGateway.create(genre);

        final var expectedName = "any name updated";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var command = UpdateGenreCommand.with(
          expectedId.getValue(),
          expectedName,
          expectedIsActive,
          asString(expectedCategories)
        );

        assertNull(genre.getDeletedAt());
        assertTrue(genre.isActive());

        final var output = useCase.execute(command);

        assertNotNull(output);
        assertEquals(expectedId.getValue(), output.id());

        final var persisted = genreRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, persisted.getName());
        assertEquals(expectedIsActive, persisted.isActive());
        assertEquals(expectedCategories.size(), persisted.getCategories().size());
        assertNotNull(persisted.getCreatedAt());
        assertNotNull(persisted.getUpdatedAt());
        assertNotNull(persisted.getDeletedAt());
    }

    @Test
    public void givenValidCommandWithCategories_whenCallsUpdateGenre_shouldReturnGenreId() {
        final var genre = Genre.newGenre("any name", true);
        final var expectedId = genre.getId();
        genreGateway.create(genre);

        final var category1 = Category.newCategory("any name", null, true);
        final var category2 = Category.newCategory("any name", null, true);
        categoryGateway.create(category1);
        categoryGateway.create(category2);

        final var expectedName = "any name updated";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of(category1.getId(), category2.getId());

        final var command = UpdateGenreCommand.with(
          expectedId.getValue(),
          expectedName,
          expectedIsActive,
          asString(expectedCategories)
        );

        final var output = useCase.execute(command);

        assertNotNull(output);
        assertEquals(expectedId.getValue(), output.id());

        final var persisted = genreRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, persisted.getName());
        assertEquals(expectedIsActive, persisted.isActive());
        assertEquals(expectedCategories.size(), persisted.getCategories().size());
        assertNotNull(persisted.getCreatedAt());
        assertNotNull(persisted.getUpdatedAt());
        assertNull(persisted.getDeletedAt());
    }

    @Test
    public void givenInvalidName_whenCallsUpdateGenreAndSomeCategoriesDoesNotExists_shouldReturnNotificationException() {
        final var movies = Category.newCategory("any name", null, true);
        categoryGateway.create(movies);

        final var series = CategoryID.from(UUID.randomUUID());
        final var documentations = CategoryID.from(UUID.randomUUID());

        final var genre = Genre.newGenre("any name", true);
        final var expectedId = genre.getId();
        genreGateway.create(genre);

        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies.getId(), series, documentations);
        final var expectedErrorMessage1 = "Some categories could not be found: %s, %s".formatted(series.toString(), documentations.toString());
        final var expectedErrorMessage2 = "Name cannot be null";
        final var expectedErrorCount = 2;

        final var command = UpdateGenreCommand.with(
          expectedId.getValue(),
          expectedName,
          expectedIsActive,
          asString(expectedCategories)
        );

        final var output = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertEquals(expectedErrorCount, output.getErrors().size());
        assertEquals(expectedErrorMessage1, output.getErrors().get(0).message());
        assertEquals(expectedErrorMessage2, output.getErrors().get(1).message());
    }

    private List<String> asString(final List<CategoryID> categories) {
        return categories.stream()
          .map(CategoryID::getValue)
          .map(UUID::toString)
          .toList();
    }
}

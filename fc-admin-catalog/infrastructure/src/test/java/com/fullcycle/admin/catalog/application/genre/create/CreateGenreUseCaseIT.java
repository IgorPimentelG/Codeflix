package com.fullcycle.admin.catalog.application.genre.create;

import com.fullcycle.admin.catalog.IntegrationTest;
import com.fullcycle.admin.catalog.domain.category.Category;
import com.fullcycle.admin.catalog.domain.category.CategoryGateway;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.errors.NotificationException;
import com.fullcycle.admin.catalog.domain.genre.GenreGateway;
import com.fullcycle.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@IntegrationTest
public class CreateGenreUseCaseIT {

    @Autowired
    private CreateGenreUseCase useCase;

    @MockitoSpyBean
    private CategoryGateway categoryGateway;

    @MockitoSpyBean
    private GenreGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void givenValidCommand_whenCallsCreateGenre_shouldReturnGenreId() {
        final var category = Category.newCategory("any name", null, true);
        categoryGateway.create(category);

        final var expectedName = "any name";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(category.getId());

        final var command = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));
        final var output = useCase.execute(command);

        assertNotNull(output);
        assertNotNull(output.genreId());

        final var persisted = genreRepository.findById(output.genreId()).get();

        assertEquals(expectedName, persisted.getName());
        assertEquals(expectedIsActive, persisted.isActive());
        assertEquals(expectedCategories.size(), persisted.getCategories().size());
        assertNotNull(persisted.getCreatedAt());
        assertNotNull(persisted.getUpdatedAt());
        assertNull(persisted.getDeletedAt());
    }

    @Test
    public void givenValidCommandWithoutCategories_whenCallsCreateGenre_shouldReturnGenreId() {
        final var expectedName = "any name";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var command = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));
        final var output = useCase.execute(command);

        assertNotNull(output);
        assertNotNull(output.genreId());

        final var persisted = genreRepository.findById(output.genreId()).get();

        assertEquals(expectedName, persisted.getName());
        assertEquals(expectedIsActive, persisted.isActive());
        assertEquals(expectedCategories.size(), persisted.getCategories().size());
        assertNotNull(persisted.getCreatedAt());
        assertNull(persisted.getUpdatedAt());
        assertNull(persisted.getDeletedAt());
    }

    @Test
    public void givenValidCommandWithInactiveGenre_whenCallsCreateGenre_shouldReturnGenreId() {
        final var expectedName = "any name";
        final var expectedIsActive = false;
        final var expectedCategories = List.< CategoryID>of();

        final var command = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));
        final var output = useCase.execute(command);

        assertNotNull(output);
        assertNotNull(output.genreId());

        final var persisted = genreRepository.findById(output.genreId()).get();

        assertEquals(expectedName, persisted.getName());
        assertEquals(expectedIsActive, persisted.isActive());
        assertEquals(expectedCategories.size(), persisted.getCategories().size());
        assertNotNull(persisted.getCreatedAt());
        assertNull(persisted.getUpdatedAt());
        assertNotNull(persisted.getDeletedAt());
    }

    @Test
    public void givenInvalidEmptyName_whenCallsCreateGenre_shouldReturnDomainException() {
        final var expectedName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();
        final var expectedErrorMessage = "Name cannot be empty";
        final var expectedErrorCount = 1;

        final var command = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));
        final var output = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertNotNull(output);
        assertEquals(expectedErrorCount, output.getErrors().size());
        assertEquals(expectedErrorMessage, output.getErrors().get(0).message());
        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).create(any());
    }

    @Test
    public void givenInvalidName_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
        final var movies = CategoryID.from(UUID.randomUUID());
        final var series = CategoryID.from(UUID.randomUUID());
        final var documentation = CategoryID.from(UUID.randomUUID());

        final var expectedName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies, series);
        final var expectedErrorMessage1 = "Some categories could not be found: %s, %s".formatted(movies.getValue().toString(), series.getValue().toString());
        final var expectedErrorMessage2 = "Name cannot be empty";
        final var expectedErrorCount = 2;

        final var command = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));
        final var output = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertNotNull(output);
        assertEquals(expectedErrorCount, output.getErrors().size());
        assertEquals(expectedErrorMessage1, output.getErrors().get(0).message());
        assertEquals(expectedErrorMessage2, output.getErrors().get(1).message());
        verify(categoryGateway, times(1)).existsByIds(any());
        verify(genreGateway, times(0)).create(any());
    }

    private List<String> asString(final List<CategoryID> categories) {
        return categories.stream()
          .map(CategoryID::getValue)
          .map(UUID::toString)
          .toList();
    }
}

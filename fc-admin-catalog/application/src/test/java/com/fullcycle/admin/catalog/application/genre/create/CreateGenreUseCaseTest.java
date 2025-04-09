package com.fullcycle.admin.catalog.application.genre.create;

import com.fullcycle.admin.catalog.application.UseCaseTest;
import com.fullcycle.admin.catalog.domain.category.CategoryGateway;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.errors.NotificationException;
import com.fullcycle.admin.catalog.domain.genre.GenreGateway;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CreateGenreUseCaseTest extends UseCaseTest {

    @Mock
    private GenreGateway genreGateway;

    @Mock
    private CategoryGateway categoryGateway;

    @InjectMocks
    private DefaultCreateGenreUseCase useCase;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway, genreGateway);
    }

    @Test
    public void givenValidCommand_whenCallsCreateGenre_shouldReturnGenreId() {
        final var expectedName = "any name";
        final var expectedIsActive = true;
        final var expectedCategories = List.< CategoryID>of();

        final var command = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        when(genreGateway.create(any())).thenAnswer(returnsFirstArg());

        final var output = useCase.execute(command);

        assertNotNull(output);
        assertNotNull(output.genreId());

        verify(genreGateway, times(1)).create(argThat(genre ->
            Objects.equals(expectedName, genre.getName()) &&
            Objects.equals(expectedIsActive, genre.isActive()) &&
            Objects.equals(expectedCategories, genre.getCategories()) &&
            Objects.nonNull(genre.getCreatedAt()) &&
            Objects.isNull(genre.getUpdatedAt()) &&
            Objects.isNull(genre.getDeletedAt())
        ));
    }

    @Test
    public void givenValidCommandWithInactiveGenre_whenCallsCreateGenre_shouldReturnGenreId() {
        final var expectedName = "any name";
        final var expectedIsActive = false;
        final var expectedCategories = List.< CategoryID>of();

        final var command = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        when(genreGateway.create(any())).thenAnswer(returnsFirstArg());

        final var output = useCase.execute(command);

        assertNotNull(output);
        assertNotNull(output.genreId());

        verify(genreGateway, times(1)).create(argThat(genre ->
          Objects.equals(expectedName, genre.getName()) &&
            Objects.equals(expectedIsActive, genre.isActive()) &&
            Objects.equals(expectedCategories, genre.getCategories()) &&
            Objects.nonNull(genre.getCreatedAt()) &&
            Objects.isNull(genre.getUpdatedAt()) &&
            Objects.nonNull(genre.getDeletedAt())
        ));
    }

    @Test
    public void givenValidCommandWithCategories_whenCallsCreateGenre_shouldReturnGenreId() {
        final var expectedName = "any name";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
          CategoryID.from(UUID.randomUUID()),
          CategoryID.from(UUID.randomUUID())
        );

        final var command = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        when(categoryGateway.existsByIds(any())).thenReturn(expectedCategories);
        when(genreGateway.create(any())).thenAnswer(returnsFirstArg());

        final var output = useCase.execute(command);

        assertNotNull(output);
        assertNotNull(output.genreId());

        verify(categoryGateway, times(1)).existsByIds(expectedCategories);
        verify(genreGateway, times(1)).create(any());
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
    public void givenInvalidNullName_whenCallsCreateGenre_shouldReturnDomainException() {
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();
        final var expectedErrorMessage = "Name cannot be null";
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
    public void givenValidCommand_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
        final var movies = CategoryID.from(UUID.randomUUID());
        final var series = CategoryID.from(UUID.randomUUID());
        final var documentation = CategoryID.from(UUID.randomUUID());

        final var expectedName = "any name";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies, series, documentation);
        final var expectedErrorMessage = "Some categories could not be found: %s, %s".formatted(movies.getValue().toString(), series.getValue().toString());
        final var expectedErrorCount = 1;

        when(categoryGateway.existsByIds(any())).thenReturn(List.of(documentation));

        final var command = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));
        final var output = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertNotNull(output);
        assertEquals(expectedErrorCount, output.getErrors().size());
        assertEquals(expectedErrorMessage, output.getErrors().get(0).message());
        verify(categoryGateway, times(1)).existsByIds(any());
        verify(genreGateway, times(0)).create(any());
    }

    @Test
    public void givenInvalidName_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
        final var movies = CategoryID.from(UUID.randomUUID());
        final var series = CategoryID.from(UUID.randomUUID());
        final var documentation = CategoryID.from(UUID.randomUUID());

        final var expectedName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies, series, documentation);
        final var expectedErrorMessage1 = "Some categories could not be found: %s, %s".formatted(movies.getValue().toString(), series.getValue().toString());
        final var expectedErrorMessage2 = "Name cannot be empty";
        final var expectedErrorCount = 2;

        when(categoryGateway.existsByIds(any())).thenReturn(List.of(documentation));

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

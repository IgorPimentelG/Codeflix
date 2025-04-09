package com.fullcycle.admin.catalog.application.genre.update;

import com.fullcycle.admin.catalog.application.UseCaseTest;
import com.fullcycle.admin.catalog.domain.category.CategoryGateway;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.errors.NotificationException;
import com.fullcycle.admin.catalog.domain.genre.Genre;
import com.fullcycle.admin.catalog.domain.genre.GenreGateway;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UpdateGenreUseCaseTest extends UseCaseTest {

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    @InjectMocks
    private DefaultUpdateGenreUseCase useCase;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway, genreGateway);
    }

    @Test
    public void givenValidCommand_whenCallsUpdateGenre_shouldReturnGenreId() {
        final var genre = Genre.newGenre("any name", true);
        final var expectedId = genre.getId();
        final var expectedName = "any name updated";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var command = UpdateGenreCommand.with(
          expectedId.getValue(),
          expectedName,
          expectedIsActive,
          asString(expectedCategories)
        );

        when(genreGateway.findById(any())).thenReturn(Optional.of(genre));
        when(genreGateway.update(any())).thenAnswer(returnsFirstArg());

        final var output = useCase.execute(command);

        assertNotNull(output);
        assertEquals(expectedId.getValue(), output.id());

        verify(genreGateway, times(1)).findById(eq(expectedId));
        verify(genreGateway, times(1)).update(argThat(genreUpdated ->
          Objects.equals(expectedName, genreUpdated.getName()) &&
          Objects.equals(expectedIsActive, genreUpdated.isActive()) &&
          Objects.equals(expectedCategories, genreUpdated.getCategories()) &&
          Objects.nonNull(genreUpdated.getCreatedAt()) &&
          Objects.nonNull(genreUpdated.getUpdatedAt()) &&
          Objects.isNull(genreUpdated.getDeletedAt())
        ));
    }

    @Test
    public void givenValidCommandWithInactiveGenre_whenCallsUpdateGenre_shouldReturnGenreId() {
        final var genre = Genre.newGenre("any name", true);
        final var expectedId = genre.getId();
        final var expectedName = "any name updated";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var command = UpdateGenreCommand.with(
          expectedId.getValue(),
          expectedName,
          expectedIsActive,
          asString(expectedCategories)
        );

        when(genreGateway.findById(any())).thenReturn(Optional.of(genre));
        when(genreGateway.update(any())).thenAnswer(returnsFirstArg());

        assertNull(genre.getDeletedAt());
        assertTrue(genre.isActive());

        final var output = useCase.execute(command);

        assertNotNull(output);
        assertEquals(expectedId.getValue(), output.id());

        verify(genreGateway, times(1)).findById(eq(expectedId));
        verify(genreGateway, times(1)).update(argThat(genreUpdated ->
          Objects.equals(expectedName, genreUpdated.getName()) &&
            Objects.equals(expectedIsActive, genreUpdated.isActive()) &&
            Objects.equals(expectedCategories, genreUpdated.getCategories()) &&
            Objects.nonNull(genreUpdated.getCreatedAt()) &&
            Objects.nonNull(genreUpdated.getUpdatedAt()) &&
            Objects.nonNull(genreUpdated.getDeletedAt())
        ));
    }

    @Test
    public void givenValidCommandWithCategories_whenCallsUpdateGenre_shouldReturnGenreId() {
        final var genre = Genre.newGenre("any name", true);
        final var expectedId = genre.getId();
        final var expectedName = "any name updated";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of(
          CategoryID.from(UUID.randomUUID()),
          CategoryID.from(UUID.randomUUID())
        );

        final var command = UpdateGenreCommand.with(
          expectedId.getValue(),
          expectedName,
          expectedIsActive,
          asString(expectedCategories)
        );

        when(categoryGateway.existsByIds(any())).thenReturn(expectedCategories);
        when(genreGateway.findById(any())).thenReturn(Optional.of(genre));
        when(genreGateway.update(any())).thenAnswer(returnsFirstArg());

        final var output = useCase.execute(command);

        assertNotNull(output);
        assertEquals(expectedId.getValue(), output.id());

        verify(genreGateway, times(1)).findById(eq(expectedId));
        verify(categoryGateway, times(1)).existsByIds(expectedCategories);
        verify(genreGateway, times(1)).update(argThat(genreUpdated ->
          Objects.equals(expectedName, genreUpdated.getName()) &&
            Objects.equals(expectedIsActive, genreUpdated.isActive()) &&
            Objects.equals(expectedCategories, genreUpdated.getCategories()) &&
            Objects.nonNull(genreUpdated.getCreatedAt()) &&
            Objects.nonNull(genreUpdated.getUpdatedAt()) &&
            Objects.isNull(genreUpdated.getDeletedAt())
        ));
    }

    @Test
    public void givenInvalidName_whenCallsUpdateGenre_shouldReturnNotificationException() {
        final var genre = Genre.newGenre("any name", true);
        final var expectedId = genre.getId();
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();
        final var expectedErrorMessage = "Name cannot be null";
        final var expectedErrorCount = 1;

        final var command = UpdateGenreCommand.with(
          expectedId.getValue(),
          expectedName,
          expectedIsActive,
          asString(expectedCategories)
        );

        when(genreGateway.findById(any())).thenReturn(Optional.of(genre));

        final var output = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertNotNull(output);

        verify(categoryGateway, times(0)).existsByIds(expectedCategories);
        verify(genreGateway, times(1)).findById(any());
        verify(genreGateway, times(0)).update(any());
        assertEquals(expectedErrorCount, output.getErrors().size());
        assertEquals(expectedErrorMessage, output.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidName_whenCallsUpdateGenreAndSomeCategoriesDoesNotExists_shouldReturnNotificationException() {
        final var movies = CategoryID.from(UUID.randomUUID());
        final var series = CategoryID.from(UUID.randomUUID());
        final var documentations = CategoryID.from(UUID.randomUUID());

        final var genre = Genre.newGenre("any name", true);
        final var expectedId = genre.getId();
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies, series, documentations);
        final var expectedErrorMessage1 = "Some categories could not be found: %s, %s".formatted(movies.getValue().toString(), series.getValue().toString());
        final var expectedErrorMessage2 = "Name cannot be null";
        final var expectedErrorCount = 2;

        final var command = UpdateGenreCommand.with(
          expectedId.getValue(),
          expectedName,
          expectedIsActive,
          asString(expectedCategories)
        );

        when(genreGateway.findById(any())).thenReturn(Optional.of(genre));
        when(categoryGateway.existsByIds(any())).thenReturn(List.of(documentations));

        final var output = assertThrows(NotificationException.class, () -> useCase.execute(command));

        verify(genreGateway, times(1)).findById(any());
        verify(categoryGateway, times(1)).existsByIds(expectedCategories);
        verify(genreGateway, times(0)).update(any());
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

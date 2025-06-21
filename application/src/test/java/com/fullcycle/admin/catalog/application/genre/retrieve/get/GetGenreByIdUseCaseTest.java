package com.fullcycle.admin.catalog.application.genre.retrieve.get;

import com.fullcycle.admin.catalog.application.UseCaseTest;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.errors.NotFoundException;
import com.fullcycle.admin.catalog.domain.genre.Genre;
import com.fullcycle.admin.catalog.domain.genre.GenreGateway;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GetGenreByIdUseCaseTest extends UseCaseTest {

    @Mock
    private GenreGateway genreGateway;

    @InjectMocks
    private DefaultGetGenreByIdUseCase useCase;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void givenValidId_whenCallsGetGenre_shouldReturnGenre() {
        final var expectedName = "any name";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
          CategoryID.from(UUID.randomUUID()),
          CategoryID.from(UUID.randomUUID())
        );

        final var genre = Genre.newGenre(expectedName, expectedIsActive);
        final var expectedId = genre.getId();
        genre.addCategories(expectedCategories);

        when(genreGateway.findById(any())).thenReturn(Optional.of(genre));

        final var output = useCase.execute(expectedId.getValue());

        assertEquals(expectedId.getValue(), output.id());
        assertEquals(expectedName, output.name());
        assertEquals(expectedIsActive, output.isActive());
        assertEquals(asString(expectedCategories), output.categories());
        assertEquals(genre.getCreatedAt(), output.createdAt());
        assertEquals(genre.getUpdatedAt(), output.updatedAt());
        assertEquals(genre.getDeletedAt(), output.deletedAt());
        verify(genreGateway, times(1)).findById(expectedId);
    }

    @Test
    public void givenValidId_whenCallsGetGenreAndDoesNotExists_shouldReturnNotFound() {
        final var expectedId = UUID.randomUUID();
        final var expectedErrorMessage = "Genre with ID %s was not found".formatted(expectedId.toString());

        when(genreGateway.findById(any())).thenReturn(Optional.empty());

        final var output = assertThrows(NotFoundException.class, () -> useCase.execute(expectedId));

        assertEquals(expectedErrorMessage, output.getMessage());
    }

    private List<String> asString(final List<CategoryID> categories) {
        return categories.stream()
          .map(CategoryID::getValue)
          .map(UUID::toString)
          .toList();
    }
}

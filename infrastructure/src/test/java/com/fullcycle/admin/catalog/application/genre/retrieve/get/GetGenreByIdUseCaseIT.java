package com.fullcycle.admin.catalog.application.genre.retrieve.get;

import com.fullcycle.admin.catalog.IntegrationTest;
import com.fullcycle.admin.catalog.domain.category.Category;
import com.fullcycle.admin.catalog.domain.category.CategoryGateway;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.errors.NotFoundException;
import com.fullcycle.admin.catalog.domain.genre.Genre;
import com.fullcycle.admin.catalog.domain.genre.GenreGateway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@IntegrationTest
public class GetGenreByIdUseCaseIT {

    @Autowired
    private GetGenreByIdUseCase useCase;

    @Autowired
    private CategoryGateway categoryGateway;

    @Autowired
    private GenreGateway genreGateway;

    @Test
    public void givenValidId_whenCallsGetGenre_shouldReturnGenre() {
        final var category = Category.newCategory("any name", null, true);
        categoryGateway.create(category);

        final var expectedName = "any name";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(category.getId());

        final var genre = Genre.newGenre(expectedName, expectedIsActive);
        final var expectedId = genre.getId();
        genre.addCategories(expectedCategories);
        genreGateway.create(genre);

        final var output = useCase.execute(expectedId.getValue());

        assertEquals(expectedId.getValue(), output.id());
        assertEquals(expectedName, output.name());
        assertEquals(expectedIsActive, output.isActive());
        assertEquals(asString(expectedCategories), output.categories());
        assertEquals(genre.getCreatedAt(), output.createdAt());
        assertEquals(genre.getUpdatedAt(), output.updatedAt());
        assertEquals(genre.getDeletedAt(), output.deletedAt());
    }

    @Test
    public void givenValidId_whenCallsGetGenreAndDoesNotExists_shouldReturnNotFound() {
        final var expectedId = UUID.randomUUID();
        final var expectedErrorMessage = "Genre with ID %s was not found".formatted(expectedId.toString());

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

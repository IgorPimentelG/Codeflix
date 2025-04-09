package com.fullcycle.admin.catalog.domain.genre;

import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.errors.NotificationException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class GenreTest {

    @Test
    public void givenValidParams_whenCallNewGenre_shouldInstantiateGenre() {
        final var expectedName = "Any name";
        final var expectedIsActive = true;
        final var expectedCategories = 0;

        final var genre = Genre.newGenre(expectedName, expectedIsActive);

        assertNotNull(genre);
        assertNotNull(genre.getId());
        assertEquals(expectedName, genre.getName());
        assertEquals(expectedIsActive, genre.isActive());
        assertEquals(expectedCategories, genre.getCategories().size());
        assertNotNull(genre.getCreatedAt());
        assertNull(genre.getUpdatedAt());
        assertNull(genre.getDeletedAt());
    }

    @Test
    public void givenInvalidNullName_whenCallNewGenreAndValidate_shouldReceiveError() {
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name cannot be null";

        final var error = assertThrows(NotificationException.class, () -> Genre.newGenre(null, true));

        assertEquals(expectedErrorCount, error.getErrors().size());
        assertEquals(expectedErrorMessage, error.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidEmptyName_whenCallNewGenreAndValidate_shouldReceiveError() {
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name cannot be empty";

        final var error = assertThrows(NotificationException.class, () -> Genre.newGenre("", true));

        assertEquals(expectedErrorCount, error.getErrors().size());
        assertEquals(expectedErrorMessage, error.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidNameWithLengthGreaterThan255_whenCallNewGenreAndValidate_shouldReceiveError() {
        final var expectedName = """
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam hendrerit nec justo at mattis. Phasellus
            est nunc, iaculis eget vehicula in, semper et mi. Curabitur justo arcu, interdum sed facilisis vel,
            viverra at nibh. Sed fringilla tortor eu nulla finibus finibus. Integer quis lorem nec enim venenatis
            vulputate eget id magna. Morbi blandit erat eget mauris sagittis lobortis. Praesent tincidunt elit quis
            ex pellentesque malesuada. Aliquam erat volutpat. In cursus et diam id tincidunt. Integer non diam 
            vulputate, rhoncus risus at, luctus sapien. Donec pharetra feugiat commodo. Curabitur pretium porta 
            neque eu dictum. Aliquam erat volutpat.
         """;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name must be between 1 and 255 characters";

        final var error = assertThrows(NotificationException.class, () -> Genre.newGenre(expectedName, true));

        assertEquals(expectedErrorCount, error.getErrors().size());
        assertEquals(expectedErrorMessage, error.getErrors().get(0).message());
    }

    @Test
    public void givenActiveGenre_whenCallInactivate_shouldReceiveOk() {
        final var expectedName = "Any name";
        final var expectedIsActive = false;
        final var expectedCategories = 0;

        final var genre = Genre.newGenre(expectedName, true);

        assertTrue(genre.isActive());
        assertNull(genre.getDeletedAt());

        genre.deactivate();

        assertNotNull(genre);
        assertNotNull(genre.getId());
        assertEquals(expectedName, genre.getName());
        assertEquals(expectedIsActive, genre.isActive());
        assertEquals(expectedCategories, genre.getCategories().size());
        assertNotNull(genre.getCreatedAt());
        assertNotNull(genre.getUpdatedAt());
        assertNotNull(genre.getDeletedAt());
    }

    @Test
    public void givenInactiveGenre_whenCallActivate_shouldReceiveOk() {
        final var expectedName = "Any name";
        final var expectedIsActive = true;
        final var expectedCategories = 0;

        final var genre = Genre.newGenre(expectedName, false);

        assertFalse(genre.isActive());
        assertNotNull(genre.getDeletedAt());

        genre.activate();

        assertNotNull(genre);
        assertNotNull(genre.getId());
        assertEquals(expectedName, genre.getName());
        assertEquals(expectedIsActive, genre.isActive());
        assertEquals(expectedCategories, genre.getCategories().size());
        assertNotNull(genre.getCreatedAt());
        assertNotNull(genre.getUpdatedAt());
        assertNull(genre.getDeletedAt());
    }

    @Test
    public void givenValidGenre_whenCallUpdate_shouldReceiveGenreUpdated() {
        final var expectedName = "Any name updated";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.from(UUID.randomUUID()));

        final var genre = Genre.newGenre("any name", false);

        assertFalse(genre.isActive());
        assertNotNull(genre.getDeletedAt());

        genre.update(expectedName, expectedIsActive, expectedCategories);

        assertNotNull(genre);
        assertNotNull(genre.getId());
        assertEquals(expectedName, genre.getName());
        assertEquals(expectedIsActive, genre.isActive());
        assertEquals(expectedCategories.size(), genre.getCategories().size());
        assertNotNull(genre.getCreatedAt());
        assertNotNull(genre.getUpdatedAt());
        assertNull(genre.getDeletedAt());
    }

    @Test
    public void givenValidInactiveGenre_whenCallUpdateWithActive_shouldReceiveGenreUpdated() {
        final var expectedName = "Any name updated";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.from(UUID.randomUUID()));

        final var genre = Genre.newGenre("any name", false);

        assertFalse(genre.isActive());
        assertNotNull(genre.getDeletedAt());

        genre.update(expectedName, expectedIsActive, expectedCategories);

        assertNotNull(genre);
        assertNotNull(genre.getId());
        assertEquals(expectedName, genre.getName());
        assertEquals(expectedIsActive, genre.isActive());
        assertEquals(expectedCategories.size(), genre.getCategories().size());
        assertNotNull(genre.getCreatedAt());
        assertNotNull(genre.getUpdatedAt());
        assertNull(genre.getDeletedAt());
    }

    @Test
    public void givenValidActiveGenre_whenCallUpdateWithInactivate_shouldReceiveGenreUpdated() {
        final var expectedName = "Any name updated";
        final var expectedIsActive = false;
        final var expectedCategories = List.of(CategoryID.from(UUID.randomUUID()));

        final var genre = Genre.newGenre("any name", true);

        assertTrue(genre.isActive());
        assertNull(genre.getDeletedAt());

        genre.update(expectedName, expectedIsActive, expectedCategories);

        assertNotNull(genre);
        assertNotNull(genre.getId());
        assertEquals(expectedName, genre.getName());
        assertEquals(expectedIsActive, genre.isActive());
        assertEquals(expectedCategories.size(), genre.getCategories().size());
        assertNotNull(genre.getCreatedAt());
        assertNotNull(genre.getUpdatedAt());
        assertNotNull(genre.getDeletedAt());
    }

    @Test
    public void givenValidGenre_whenCallUpdateWithEmptyName_shouldReceiveError() {
        final var expectedIsActive = false;
        final var expectedCategories = List.of(CategoryID.from(UUID.randomUUID()));
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name cannot be empty";

        final var genre = Genre.newGenre("any name", true);

        final var error = assertThrows(NotificationException.class, () ->
          genre.update("", expectedIsActive, expectedCategories)
        );

        assertEquals(expectedErrorCount, error.getErrors().size());
        assertEquals(expectedErrorMessage, error.getErrors().get(0).message());
    }

    @Test
    public void givenValidGenre_whenCallUpdateWithNullName_shouldReceiveError() {
        final var expectedIsActive = false;
        final var expectedCategories = List.of(CategoryID.from(UUID.randomUUID()));
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name cannot be null";

        final var genre = Genre.newGenre("any name", true);

        final var error = assertThrows(NotificationException.class, () ->
          genre.update(null, expectedIsActive, expectedCategories)
        );

        assertEquals(expectedErrorCount, error.getErrors().size());
        assertEquals(expectedErrorMessage, error.getErrors().get(0).message());
    }

    @Test
    public void givenValidGenre_whenCallUpdateWithNullCategories_shouldReceiveOk() {
        final var expectedName = "Any name updated";
        final var expectedIsActive = true;
        final var expectedCategories = new ArrayList<CategoryID>();

        final var genre = Genre.newGenre("any name", false);

        assertDoesNotThrow(() -> genre.update(expectedName, expectedIsActive, null));

        assertNotNull(genre);
        assertNotNull(genre.getId());
        assertEquals(expectedName, genre.getName());
        assertEquals(expectedIsActive, genre.isActive());
        assertEquals(expectedCategories, genre.getCategories());
        assertNotNull(genre.getCreatedAt());
        assertNotNull(genre.getUpdatedAt());
        assertNull(genre.getDeletedAt());
    }

    @Test
    public void givenValidEmptyCategoriesGenre_whenCallAddCategory_shouldReceiveOk() {
        final var seriesID = CategoryID.from(UUID.randomUUID());
        final var moviesID = CategoryID.from(UUID.randomUUID());
        final var expectedName = "Any name";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(seriesID, moviesID);

        final var genre = Genre.newGenre(expectedName, expectedIsActive);

        genre.addCategory(seriesID);
        genre.addCategory(moviesID);

        assertNotNull(genre);
        assertNotNull(genre.getId());
        assertEquals(expectedName, genre.getName());
        assertEquals(expectedIsActive, genre.isActive());
        assertNotNull(genre.getCreatedAt());
        assertNotNull(genre.getUpdatedAt());
        assertNull(genre.getDeletedAt());
        assertEquals(2, genre.getCategories().size());
        assertEquals(genre.getCategories(), expectedCategories);
    }

    @Test
    public void givenValidGenreWithTwoCategories_whenCallRemoveCategory_shouldReceiveOk() {
        final var seriesID = CategoryID.from(UUID.randomUUID());
        final var moviesID = CategoryID.from(UUID.randomUUID());
        final var expectedName = "Any name";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(moviesID);

        final var genre = Genre.newGenre(expectedName, expectedIsActive);

        genre.update(expectedName, expectedIsActive, List.of(seriesID, moviesID));

        assertEquals(2, genre.getCategories().size());

        genre.removeCategory(seriesID);

        assertNotNull(genre);
        assertNotNull(genre.getId());
        assertEquals(expectedName, genre.getName());
        assertEquals(expectedIsActive, genre.isActive());
        assertNotNull(genre.getCreatedAt());
        assertNotNull(genre.getUpdatedAt());
        assertNull(genre.getDeletedAt());
        assertEquals(1, genre.getCategories().size());
        assertEquals(genre.getCategories(), expectedCategories);
    }

    @Test
    public void givenInvalidNullCategoryID_whenCallAddCategory_shouldReceiveOk() {
        final var expectedName = "Any name";
        final var expectedIsActive = true;
        final var expectedCategories = new ArrayList<CategoryID>();

        final var genre = Genre.newGenre(expectedName, expectedIsActive);

        genre.update(expectedName, expectedIsActive, expectedCategories);

        assertEquals(0, genre.getCategories().size());

        genre.addCategory(null);

        assertNotNull(genre);
        assertNotNull(genre.getId());
        assertEquals(expectedName, genre.getName());
        assertEquals(expectedIsActive, genre.isActive());
        assertNotNull(genre.getCreatedAt());
        assertNotNull(genre.getUpdatedAt());
        assertNull(genre.getDeletedAt());
        assertEquals(0, genre.getCategories().size());
        assertEquals(genre.getCategories(), expectedCategories);
    }

    @Test
    public void givenInvalidNullCategoryID_whenCallRemoveCategory_shouldReceiveOk() {
        final var seriesID = CategoryID.from(UUID.randomUUID());
        final var moviesID = CategoryID.from(UUID.randomUUID());
        final var expectedName = "Any name";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(seriesID, moviesID);

        final var genre = Genre.newGenre(expectedName, expectedIsActive);

        genre.update(expectedName, expectedIsActive, expectedCategories);

        assertEquals(2, genre.getCategories().size());

        genre.removeCategory(null);

        assertNotNull(genre);
        assertNotNull(genre.getId());
        assertEquals(expectedName, genre.getName());
        assertEquals(expectedIsActive, genre.isActive());
        assertNotNull(genre.getCreatedAt());
        assertNotNull(genre.getUpdatedAt());
        assertNull(genre.getDeletedAt());
        assertEquals(2, genre.getCategories().size());
        assertEquals(genre.getCategories(), expectedCategories);
    }

    @Test
    public void givenEmptyCategoriesGenre_whenCallAddCategories_shouldReceiveOk() {
        final var seriesID = CategoryID.from(UUID.randomUUID());
        final var moviesID = CategoryID.from(UUID.randomUUID());
        final var expectedName = "Any name";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(seriesID, moviesID);

        final var genre = Genre.newGenre(expectedName, expectedIsActive);
        genre.addCategories(expectedCategories);

        assertNotNull(genre);
        assertNotNull(genre.getId());
        assertEquals(expectedName, genre.getName());
        assertEquals(expectedIsActive, genre.isActive());
        assertNotNull(genre.getCreatedAt());
        assertNotNull(genre.getUpdatedAt());
        assertNull(genre.getDeletedAt());
        assertEquals(2, genre.getCategories().size());
        assertEquals(genre.getCategories(), expectedCategories);
    }

    @Test
    public void givenEmptyCategoriesGenre_whenCallAddCategoriesWithEmptyList_shouldReceiveOk() {
        final var expectedName = "Any name";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var genre = Genre.newGenre(expectedName, expectedIsActive);
        genre.addCategories(expectedCategories);

        assertNotNull(genre);
        assertNotNull(genre.getId());
        assertEquals(expectedName, genre.getName());
        assertEquals(expectedIsActive, genre.isActive());
        assertNotNull(genre.getCreatedAt());
        assertNull(genre.getUpdatedAt());
        assertNull(genre.getDeletedAt());
        assertEquals(0, genre.getCategories().size());
        assertEquals(genre.getCategories(), expectedCategories);
    }

    @Test
    public void givenEmptyCategoriesGenre_whenCallAddCategoriesWithNullList_shouldReceiveOk() {
        final var expectedName = "Any name";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var genre = Genre.newGenre(expectedName, expectedIsActive);
        genre.addCategories(null);

        assertNotNull(genre);
        assertNotNull(genre.getId());
        assertEquals(expectedName, genre.getName());
        assertEquals(expectedIsActive, genre.isActive());
        assertNotNull(genre.getCreatedAt());
        assertNull(genre.getUpdatedAt());
        assertNull(genre.getDeletedAt());
        assertEquals(0, genre.getCategories().size());
        assertEquals(genre.getCategories(), expectedCategories);
    }
}
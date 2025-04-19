package com.fullcycle.admin.catalog.e2e.genre;

import com.fullcycle.admin.catalog.E2ETest;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.e2e.MockDsl;
import com.fullcycle.admin.catalog.infrastructure.genre.models.UpdateGenreRequest;
import com.fullcycle.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
@Testcontainers
public class GenreE2ETest implements MockDsl {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private GenreRepository genreRepository;

    @Container
    private static final MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>("mysql:latest")
      .withDatabaseName("adm_videos")
      .withUsername("root")
      .withPassword("admin")
      .withEnv("MYSQL_CHARACTER_SET_SERVER", "utf8mb4")
      .withEnv("MYSQL_COLLATION_SERVER", "utf8mb4_unicode_ci");

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        final var port = MYSQL_CONTAINER.getMappedPort(3306);
        registry.add("mysql.port", () -> port);
    }

    @BeforeEach
    public void testConnection() {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
    }

    @Override
    public MockMvc mvc() {
        return mvc;
    }

    @Test
    public void asGenreAdminShouldBeAbleToCreateNewGenreWithValidValues() throws Exception {
        final var categoryId = givenCategory("any name", "any description", true);
        final var expectedName = "any name";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(categoryId);

        final var id = givenGenre(expectedName, expectedIsActive, expectedCategories);
        final var genre = genreRepository.findById(id.getValue()).get();

        assertNotNull(genre.getId());
        assertEquals(expectedName, genre.getName());
        assertEquals(expectedIsActive, genre.isActive());
        assertEquals(expectedCategories.size(), genre.getCategories().size());
        assertNotNull(genre.getCreatedAt());
        assertNotNull(genre.getUpdatedAt());
        assertNull(genre.getDeletedAt());
    }

    @Test
    public void asGenreAdminShouldBeAbleToCreateNewGenreWithValidValuesWithCategories() throws Exception {
        final var expectedName = "any name";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var id = givenGenre(expectedName, expectedIsActive, expectedCategories);
        final var genre = genreRepository.findById(id.getValue()).get();

        assertNotNull(genre.getId());
        assertEquals(expectedName, genre.getName());
        assertEquals(expectedIsActive, genre.isActive());
        assertNotNull(genre.getCreatedAt());
        assertNull(genre.getUpdatedAt());
        assertNull(genre.getDeletedAt());
    }

    @Test
    public void asGenreAdminShouldBeAbleToNavigateToAllGenres() throws Exception {
        givenGenre("any name 1", true, List.of());
        givenGenre("any name 2", true, List.of());
        givenGenre("any name 3", true, List.of());

        listGenres(0, 1)
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.current_page", equalTo(0)))
          .andExpect(jsonPath("$.per_page", equalTo(1)))
          .andExpect(jsonPath("$.total", equalTo(3)))
          .andExpect(jsonPath("$.items", hasSize(1)))
          .andExpect(jsonPath("$.items[0].name", equalTo("any name 3")));

        listGenres(1, 1)
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.current_page", equalTo(1)))
          .andExpect(jsonPath("$.per_page", equalTo(1)))
          .andExpect(jsonPath("$.total", equalTo(3)))
          .andExpect(jsonPath("$.items", hasSize(1)))
          .andExpect(jsonPath("$.items[0].name", equalTo("any name 2")));

        listGenres(2, 1)
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.current_page", equalTo(2)))
          .andExpect(jsonPath("$.per_page", equalTo(1)))
          .andExpect(jsonPath("$.total", equalTo(3)))
          .andExpect(jsonPath("$.items", hasSize(1)))
          .andExpect(jsonPath("$.items[0].name", equalTo("any name 1")));

        listGenres(3, 1)
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.current_page", equalTo(3)))
          .andExpect(jsonPath("$.per_page", equalTo(1)))
          .andExpect(jsonPath("$.total", equalTo(3)))
          .andExpect(jsonPath("$.items", hasSize(0)));
    }

    @Test
    public void asGenreAdminShouldBeAbleToSearchBetweenAllGenres() throws Exception {
        givenGenre("any name 1", true, List.of());
        givenGenre("any name 2", true, List.of());
        givenGenre("any name 3", true, List.of());

        listGenres(0, 1, "1")
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.current_page", equalTo(0)))
          .andExpect(jsonPath("$.per_page", equalTo(1)))
          .andExpect(jsonPath("$.total", equalTo(1)))
          .andExpect(jsonPath("$.items", hasSize(1)))
          .andExpect(jsonPath("$.items[0].name", equalTo("any name 1")));
    }

    @Test
    public void asGenreAdminShouldBeAbleToSortAllGenresByNameDesc() throws Exception {
        givenGenre("any name 1", true, List.of());
        givenGenre("any name 2", true, List.of());
        givenGenre("any name 3", true, List.of());

        listGenres(0, 3, "desc", "", "name")
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.current_page", equalTo(0)))
          .andExpect(jsonPath("$.per_page", equalTo(3)))
          .andExpect(jsonPath("$.total", equalTo(3)))
          .andExpect(jsonPath("$.items", hasSize(3)))
          .andExpect(jsonPath("$.items[0].name", equalTo("any name 3")))
          .andExpect(jsonPath("$.items[1].name", equalTo("any name 2")))
          .andExpect(jsonPath("$.items[2].name", equalTo("any name 1")));
    }

    @Test
    public void asGenreAdminShouldBeAbleToGetGenreByItsIdentifier() throws Exception {
        final var categoryId = givenCategory("any name", "any description", true);
        final var expectedName = "any name";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(categoryId);

        final var id = givenGenre(expectedName, expectedIsActive, expectedCategories);
        final var genre = retrieveGenre(id);

        assertNotNull(genre.id());
        assertEquals(expectedName, genre.name());
        assertEquals(expectedIsActive, genre.isActive());
        assertEquals(expectedCategories.size(), genre.categories().size());
        assertNotNull(genre.createdAt());
        assertNotNull(genre.updatedAt());
        assertNull(genre.deletedAt());
    }

    @Test
    public void asGenreAdminShouldBeAbleToSeeTreatedErrorByGettingNotFoundGenre() throws Exception {
        final var id = UUID.randomUUID().toString();
        final var expectedErrorMessage = "Genre with ID %s was not found".formatted(id);

        final var request = MockMvcRequestBuilders.get("/genres/" + id).contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void asGenreAdminShouldBeAbleToUpdateGenreByItsIdentifier() throws Exception {
        final var categoryId = givenCategory("any name", "any description", true);
        final var expectedName = "any name updated";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(categoryId);
        final var id = givenGenre("any name", false, expectedCategories);

        final var categoriesMapped = mapTo(expectedCategories, (item) -> item.getValue().toString());
        final var input = new UpdateGenreRequest(expectedName, categoriesMapped, expectedIsActive);

        updateGenre(id, input).andExpect(status().isOk());

        final var genre = genreRepository.findById(id.getValue()).get();

        assertEquals(id.getValue().toString(), genre.getId().toString());
        assertEquals(expectedName, genre.getName());
        assertEquals(expectedIsActive, genre.isActive());
        assertEquals(expectedCategories.size(), genre.getCategories().size());
        assertNotNull(genre.getCreatedAt());
        assertNotNull(genre.getUpdatedAt());
        assertNull(genre.getDeletedAt());
    }

    @Test
    public void asCategoryAdminShouldBeAbleToDeleteCategoryByItsIdentifier() throws Exception {
        final var id = givenGenre("any name", true, List.of());
        deleteGenre(id).andExpect(status().isNoContent());
        assertFalse(genreRepository.existsById(id.getValue()));
    }
}

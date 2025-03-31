package com.fullcycle.admin.catalog.e2e.category;

import com.fullcycle.admin.catalog.E2ETest;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.infrastructure.category.models.CategoryResponse;
import com.fullcycle.admin.catalog.infrastructure.category.models.CreateCategoryRequest;
import com.fullcycle.admin.catalog.infrastructure.category.models.UpdateCategoryRequest;
import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import com.fullcycle.admin.catalog.infrastructure.configuration.json.Json;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
@Testcontainers
public class CategoryE2ETest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CategoryRepository categoryRepository;

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

    @Test
    public void asCategoryAdminShouldBeAbleToCreateNewCategoryWithValidValues() throws Exception {
        final var expectedName = "any name";
        final var expectedDescription = "any description";
        final var expectedIsActive = true;

        final var id = givenCategory(expectedName, expectedDescription, expectedIsActive);
        final var category = retrieveCategory(id.getValue());

        assertNotNull(category.id());
        assertEquals(expectedName, category.name());
        assertEquals(expectedDescription, category.description());
        assertEquals(expectedIsActive, category.active());
        assertNotNull(category.createdAt());
        assertNull(category.updatedAt());
        assertNull(category.deletedAt());
    }

    @Test
    public void asCategoryAdminShouldBeAbleToNavigateToAllCategories() throws Exception {
        givenCategory("any name 1", null, true);
        givenCategory("any name 2", null, true);
        givenCategory("any name 3", null, true);

        listCategories(0, 1)
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.current_page", equalTo(0)))
          .andExpect(jsonPath("$.per_page", equalTo(1)))
          .andExpect(jsonPath("$.total", equalTo(3)))
          .andExpect(jsonPath("$.items", hasSize(1)))
          .andExpect(jsonPath("$.items[0].name", equalTo("any name 3")));

        listCategories(1, 1)
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.current_page", equalTo(1)))
          .andExpect(jsonPath("$.per_page", equalTo(1)))
          .andExpect(jsonPath("$.total", equalTo(3)))
          .andExpect(jsonPath("$.items", hasSize(1)))
          .andExpect(jsonPath("$.items[0].name", equalTo("any name 2")));

        listCategories(2, 1)
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.current_page", equalTo(2)))
          .andExpect(jsonPath("$.per_page", equalTo(1)))
          .andExpect(jsonPath("$.total", equalTo(3)))
          .andExpect(jsonPath("$.items", hasSize(1)))
          .andExpect(jsonPath("$.items[0].name", equalTo("any name 1")));

        listCategories(3, 1)
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.current_page", equalTo(3)))
          .andExpect(jsonPath("$.per_page", equalTo(1)))
          .andExpect(jsonPath("$.total", equalTo(3)))
          .andExpect(jsonPath("$.items", hasSize(0)));
    }

    @Test
    public void asCategoryAdminShouldBeAbleToSearchBetweenAllCategories() throws Exception {
        givenCategory("any name 1", null, true);
        givenCategory("any name 2", null, true);
        givenCategory("any name 3", null, true);

        listCategories(0, 1, "1")
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.current_page", equalTo(0)))
          .andExpect(jsonPath("$.per_page", equalTo(1)))
          .andExpect(jsonPath("$.total", equalTo(1)))
          .andExpect(jsonPath("$.items", hasSize(1)))
          .andExpect(jsonPath("$.items[0].name", equalTo("any name 1")));
    }

    @Test
    public void asCategoryAdminShouldBeAbleToSortAllCategoriesByDescriptionDesc() throws Exception {
        givenCategory("any name 1", "any description 1", true);
        givenCategory("any name 2", "any description 2", true);
        givenCategory("any name 3", "any description 3", true);

        listCategories(0, 3, "", "description", "desc")
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
    public void asCategoryAdminShouldBeAbleToGetCategoryByItsIdentifier() throws Exception {
        final var expectedName = "any name";
        final var expectedDescription = "any description";
        final var expectedIsActive = true;

        final var id = givenCategory(expectedName, expectedDescription, expectedIsActive);
        final var category = retrieveCategory(id.getValue());

        assertNotNull(category.id());
        assertEquals(expectedName, category.name());
        assertEquals(expectedDescription, category.description());
        assertEquals(expectedIsActive, category.active());
        assertNotNull(category.createdAt());
        assertNull(category.updatedAt());
        assertNull(category.deletedAt());
    }

    @Test
    public void asCategoryAdminShouldBeAbleToSeeTreatedErrorByGettingNotFoundCategory() throws Exception {
        final var id = UUID.randomUUID().toString();
        final var expectedErrorMessage = "Category with ID %s was not found".formatted(id);

        final var request = MockMvcRequestBuilders.get("/categories/" + id).contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void asCategoryAdminShouldBeAbleToUpdateCategoryByItsIdentifier() throws Exception {
        final var id = givenCategory("Any name", null, false);

        final var expectedName = "any name updated";
        final var expectedDescription = "any description updated";
        final var expectedIsActive = true;

        final var input = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        final var request = MockMvcRequestBuilders.put("/categories/" + id.getValue().toString())
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .content(Json.writeValueAsString(input));

       mvc.perform(request).andExpect(status().isOk());

       final var category = categoryRepository.findById(id.getValue()).get();

        assertEquals(id.getValue().toString(), category.getId().toString());
        assertEquals(expectedName, category.getName());
        assertEquals(expectedDescription, category.getDescription());
        assertEquals(expectedIsActive, category.isActive());
        assertNotNull(category.getCreatedAt());
        assertNotNull(category.getUpdatedAt());
        assertNull(category.getDeletedAt());
    }

    @Test
    public void asCategoryAdminShouldBeAbleToDeleteCategoryByItsIdentifier() throws Exception {
        final var id = givenCategory("any name", null, true);

        final var request = MockMvcRequestBuilders.delete("/categories/" + id.getValue().toString())
          .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request).andExpect(status().isNoContent());

        assertFalse(categoryRepository.existsById(id.getValue()));
    }

    private ResultActions listCategories(final int page, final int perPage) throws Exception {
        return listCategories(page, perPage, "", "", "");
    }

    private ResultActions listCategories(final int page, final int perPage, String search) throws Exception {
        return listCategories(page, perPage, "", "", search);
    }

    private ResultActions listCategories(
      final int page,
      final int perPage,
      final String dir,
      final String sort,
      final String search
    ) throws Exception {
        final var request = MockMvcRequestBuilders.get("/categories")
          .queryParam("page", String.valueOf(page))
          .queryParam("perPage", String.valueOf(perPage))
          .queryParam("dir", dir)
          .queryParam("sort", sort)
          .queryParam("search", search)
          .accept(MediaType.APPLICATION_JSON)
          .contentType(MediaType.APPLICATION_JSON);
       return mvc.perform(request);
    }

    private CategoryResponse retrieveCategory(final UUID id) throws Exception {
        final var request = MockMvcRequestBuilders.get("/categories/" + id.toString()).contentType(MediaType.APPLICATION_JSON);

        final var json = mvc.perform(request)
          .andExpect(status().isOk())
          .andReturn()
          .getResponse().getContentAsString();

        return Json.readValue(json, CategoryResponse.class);
    }

    private CategoryID givenCategory(final String name, final String description, final boolean isActive) throws Exception {
        final var input = new CreateCategoryRequest(name, description, isActive);

        final var request = MockMvcRequestBuilders.post("/categories")
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .content(Json.writeValueAsString(input));

        final var id = mvc.perform(request)
          .andExpect(status().isCreated())
          .andReturn()
          .getResponse().getHeader("Location")
          .replace("/categories/", "");

        return CategoryID.from(id);
    }
}

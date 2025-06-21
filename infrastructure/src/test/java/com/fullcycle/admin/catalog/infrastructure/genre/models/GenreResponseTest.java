package com.fullcycle.admin.catalog.infrastructure.genre.models;

import com.fullcycle.admin.catalog.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@JacksonTest
public class GenreResponseTest {

    @Autowired
    private JacksonTester<GenreResponse> json;

    @Test
    public void testMarshall() throws IOException {
        final var expectedId = UUID.randomUUID().toString();
        final var expectedName = "any name";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(UUID.randomUUID().toString());
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = Instant.now();
        final var expectedDeletedAt = Instant.now();

        final var response = new GenreResponse(
          expectedId,
          expectedName,
          expectedCategories,
          expectedIsActive,
          expectedCreatedAt,
          expectedUpdatedAt,
          expectedDeletedAt
        );

        final var result = json.write(response);

        Assertions.assertThat(result)
          .hasJsonPathValue("$.id", expectedId)
          .hasJsonPathValue("$.name", expectedName)
          .hasJsonPathValue("$.categories_id", expectedCategories)
          .hasJsonPathValue("$.is_active", expectedIsActive)
          .hasJsonPathValue("$.created_at", expectedCreatedAt.toString())
          .hasJsonPathValue("$.updated_at", expectedUpdatedAt.toString())
          .hasJsonPathValue("$.deleted_at", expectedDeletedAt.toString());
    }

    @Test
    public void testUnmarshall() throws IOException {
        final var expectedId = UUID.randomUUID().toString();
        final var expectedName = "any name";
        final var expectedIsActive = true;
        final var expectedCategories = UUID.randomUUID().toString();
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = Instant.now();
        final var expectedDeletedAt = Instant.now();

        final var content = """
          {
            "id": "%s",
            "name": "%s",
            "categories_id": ["%s"],
            "is_active": %s,
            "created_at": "%s",
            "updated_at": "%s",
            "deleted_at": "%s"
          }
          """.formatted(
          expectedId,
          expectedName,
          expectedCategories,
          expectedIsActive,
          expectedCreatedAt.toString(),
          expectedUpdatedAt.toString(),
          expectedDeletedAt.toString()
        );

        final var result = json.parse(content);

        Assertions.assertThat(result)
          .hasFieldOrPropertyWithValue("id", expectedId)
          .hasFieldOrPropertyWithValue("name", expectedName)
          .hasFieldOrPropertyWithValue("categories", List.of(expectedCategories))
          .hasFieldOrPropertyWithValue("isActive", expectedIsActive)
          .hasFieldOrPropertyWithValue("createdAt", expectedCreatedAt)
          .hasFieldOrPropertyWithValue("updatedAt", expectedUpdatedAt)
          .hasFieldOrPropertyWithValue("deletedAt", expectedDeletedAt);
    }
}

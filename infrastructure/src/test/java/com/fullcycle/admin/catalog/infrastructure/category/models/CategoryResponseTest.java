package com.fullcycle.admin.catalog.infrastructure.category.models;

import com.fullcycle.admin.catalog.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@JacksonTest
public class CategoryResponseTest {

    @Autowired
    private JacksonTester<CategoryResponse> json;

    @Test
    public void testMarshall() throws IOException {
        final var expectedId = UUID.randomUUID().toString();
        final var expectedName = "any name";
        final var expectedDescription = "any description";
        final var expectedIsActive = true;
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = Instant.now();
        final var expectedDeletedAt = Instant.now();

        final var response = new CategoryResponse(
          expectedId,
          expectedName,
          expectedDescription,
          expectedIsActive,
          expectedCreatedAt,
          expectedUpdatedAt,
          expectedDeletedAt
        );

        final var result = json.write(response);

        Assertions.assertThat(result)
          .hasJsonPathValue("$.id", expectedId)
          .hasJsonPathValue("$.name", expectedName)
          .hasJsonPathValue("$.description", expectedDescription)
          .hasJsonPathValue("$.is_active", expectedIsActive)
          .hasJsonPathValue("$.created_at", expectedCreatedAt.toString())
          .hasJsonPathValue("$.updated_at", expectedUpdatedAt.toString())
          .hasJsonPathValue("$.deleted_at", expectedDeletedAt.toString());
    }

    @Test
    public void testUnmarshall() throws IOException {
        final var expectedId = UUID.randomUUID().toString();
        final var expectedName = "any name";
        final var expectedDescription = "any description";
        final var expectedIsActive = true;
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = Instant.now();
        final var expectedDeletedAt = Instant.now();

        final var content = """
          {
            "id": "%s",
            "name": "%s",
            "description": "%s",
            "is_active": %s,
            "created_at": "%s",
            "updated_at": "%s",
            "deleted_at": "%s"
          }
          """.formatted(
            expectedId,
              expectedName,
              expectedDescription,
              expectedIsActive,
              expectedCreatedAt.toString(),
              expectedUpdatedAt.toString(),
              expectedDeletedAt.toString()
         );

        final var result = json.parse(content);

        Assertions.assertThat(result)
          .hasFieldOrPropertyWithValue("id", expectedId)
          .hasFieldOrPropertyWithValue("name", expectedName)
          .hasFieldOrPropertyWithValue("description", expectedDescription)
          .hasFieldOrPropertyWithValue("active", expectedIsActive)
          .hasFieldOrPropertyWithValue("createdAt", expectedCreatedAt)
          .hasFieldOrPropertyWithValue("updatedAt", expectedUpdatedAt)
          .hasFieldOrPropertyWithValue("deletedAt", expectedDeletedAt);
    }
}

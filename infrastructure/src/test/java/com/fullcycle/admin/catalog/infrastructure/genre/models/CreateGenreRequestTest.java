package com.fullcycle.admin.catalog.infrastructure.genre.models;

import com.fullcycle.admin.catalog.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@JacksonTest
public class CreateGenreRequestTest {

    @Autowired
    private JacksonTester<CreateGenreRequest> json;

    @Test
    public void testMarshall() throws IOException {
        final var expectedName = "any name";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(UUID.randomUUID().toString());

        final var response = new CreateGenreRequest(
          expectedName,
          expectedCategories,
          expectedIsActive
        );

        final var result = json.write(response);

        Assertions.assertThat(result)
          .hasJsonPathValue("$.name", expectedName)
          .hasJsonPathValue("$.categories_id", expectedCategories)
          .hasJsonPathValue("$.is_active", expectedIsActive);
    }

    @Test
    public void testUnmarshall() throws IOException {
        final var expectedName = "any name";
        final var expectedIsActive = true;
        final var expectedCategory = UUID.randomUUID().toString();

        final var content = """
          {
            "name": "%s",
            "categories_id": ["%s"],
            "is_active": %s
          }
          """.formatted(
          expectedName,
          expectedCategory,
          expectedIsActive
        );

        final var result = json.parse(content);

        Assertions.assertThat(result)
          .hasFieldOrPropertyWithValue("name", expectedName)
          .hasFieldOrPropertyWithValue("categories", List.of(expectedCategory))
          .hasFieldOrPropertyWithValue("isActive", expectedIsActive);
    }
}

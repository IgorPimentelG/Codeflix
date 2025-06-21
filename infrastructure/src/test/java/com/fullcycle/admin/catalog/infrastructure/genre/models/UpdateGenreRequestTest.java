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
public class UpdateGenreRequestTest {

    @Autowired
    private JacksonTester<UpdateGenreRequest> json;

    @Test
    public void testUnmarshall() throws IOException {
        final var expectedName = "any name";
        final var expectedIsActive = true;
        final var expectedCategories = UUID.randomUUID().toString();

        final var content = """
          {
            "name": "%s",
            "categories_id": ["%s"],
            "is_active": %s
          }
          """.formatted(
          expectedName,
          expectedCategories,
          expectedIsActive
        );

        final var result = json.parse(content);

        Assertions.assertThat(result)
          .hasFieldOrPropertyWithValue("name", expectedName)
          .hasFieldOrPropertyWithValue("categories", List.of(expectedCategories))
          .hasFieldOrPropertyWithValue("isActive", expectedIsActive);
    }
}

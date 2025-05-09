package com.fullcycle.admin.catalog.infrastructure.category.models;

import com.fullcycle.admin.catalog.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

@JacksonTest
public class UpdateCategoryRequestTest {

    @Autowired
    private JacksonTester<UpdateCategoryRequest> json;

    @Test
    public void testUnmarshall() throws IOException {
        final var expectedName = "any name";
        final var expectedDescription = "any description";
        final var expectedIsActive = true;

        final var content = """
          {
            "name": "%s",
            "description": "%s",
            "is_active": %s
          }
          """.formatted(
              expectedName,
              expectedDescription,
              expectedIsActive
         );

        final var result = json.parse(content);

        Assertions.assertThat(result)
          .hasFieldOrPropertyWithValue("name", expectedName)
          .hasFieldOrPropertyWithValue("description", expectedDescription)
          .hasFieldOrPropertyWithValue("active", expectedIsActive);
    }
}

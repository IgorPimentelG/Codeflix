package com.fullcycle.admin.catalog.infrastructure.genre.models;

import com.fullcycle.admin.catalog.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@JacksonTest
public class GenreListResponseTest {

    @Autowired
    private JacksonTester<GenreListResponse> json;

    @Test
    public void testMarshall() throws IOException {
        final var expectedId = UUID.randomUUID().toString();
        final var expectedName = "any name";
        final var expectedIsActive = true;
        final var expectedCreatedAt = Instant.now();
        final var expectedDeletedAt = Instant.now();

        final var response = new GenreListResponse(
          expectedId,
          expectedName,
          expectedIsActive,
          expectedCreatedAt,
          expectedDeletedAt
        );

        final var result = json.write(response);

        Assertions.assertThat(result)
          .hasJsonPathValue("$.id", expectedId)
          .hasJsonPathValue("$.name", expectedName)
          .hasJsonPathValue("$.is_active", expectedIsActive)
          .hasJsonPathValue("$.created_at", expectedCreatedAt.toString())
          .hasJsonPathValue("$.deleted_at", expectedDeletedAt.toString());
    }
}

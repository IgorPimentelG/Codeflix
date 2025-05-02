package com.fullcycle.admin.catalog.infrastructure.castmember.models;

import com.fullcycle.admin.catalog.Fixture;
import com.fullcycle.admin.catalog.JacksonTest;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.Instant;

@JacksonTest
public class CastMemberResponseTest {

    @Autowired
    private JacksonTester<CastMemberResponse> json;

    @Test
    public void testMarshall() throws IOException {
        final var expectedId = CastMemberID.unique();
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = Instant.now();

        final var response = new CastMemberResponse(expectedId.getValue().toString(), expectedName, expectedType, expectedCreatedAt, expectedUpdatedAt);

        final var result = json.write(response);

        Assertions.assertThat(result)
          .hasJsonPathValue("$.id", expectedId.getValue().toString())
          .hasJsonPathValue("$.name", expectedName)
          .hasJsonPathValue("$.type", expectedType.name())
          .hasJsonPathValue("$.created_at", expectedCreatedAt.toString())
          .hasJsonPathValue("$.updated_at", expectedUpdatedAt.toString());
    }

    @Test
    public void testUnmarshall() throws IOException {
        final var expectedId = CastMemberID.unique();
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = Instant.now();

        final var content = """
          {
            "id": "%s",
            "name": "%s",
            "type": "%s",
            "created_at": "%s",
            "updated_at": "%s"
          }
          """.formatted(
          expectedId.getValue().toString(),
          expectedName,
          expectedType.name(),
          expectedCreatedAt.toString(),
          expectedUpdatedAt.toString()
        );

        final var result = json.parse(content);

        Assertions.assertThat(result)
          .hasFieldOrPropertyWithValue("id", expectedId.getValue().toString())
          .hasFieldOrPropertyWithValue("name", expectedName)
          .hasFieldOrPropertyWithValue("type", expectedType)
          .hasFieldOrPropertyWithValue("createdAt", expectedCreatedAt)
          .hasFieldOrPropertyWithValue("updatedAt", expectedUpdatedAt);
    }
}

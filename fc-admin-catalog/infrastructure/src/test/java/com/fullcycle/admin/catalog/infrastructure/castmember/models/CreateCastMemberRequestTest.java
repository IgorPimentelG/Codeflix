package com.fullcycle.admin.catalog.infrastructure.castmember.models;

import com.fullcycle.admin.catalog.Fixture;
import com.fullcycle.admin.catalog.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

@JacksonTest
public class CreateCastMemberRequestTest {

    @Autowired
    private JacksonTester<CreateCastMemberRequest> json;

    @Test
    public void testMarshall() throws IOException {
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var response = new CreateCastMemberRequest(expectedName, expectedType);

        final var result = json.write(response);

        Assertions.assertThat(result)
          .hasJsonPathValue("$.name", expectedName)
          .hasJsonPathValue("$.type", expectedType.name());
    }

    @Test
    public void testUnmarshall() throws IOException {
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var content = """
          {
            "name": "%s",
            "type": "%s"
          }
          """.formatted(expectedName, expectedType.name());

        final var result = json.parse(content);

        Assertions.assertThat(result)
          .hasFieldOrPropertyWithValue("name", expectedName)
          .hasFieldOrPropertyWithValue("type", expectedType);
    }
}

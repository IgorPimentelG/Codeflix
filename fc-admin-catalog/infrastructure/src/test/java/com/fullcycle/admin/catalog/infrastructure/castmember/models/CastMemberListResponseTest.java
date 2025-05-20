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
public class CastMemberListResponseTest {

    @Autowired
    private JacksonTester<CastMemberListResponse> json;

    @Test
    public void testMarshall() throws IOException {
        final var expectedId = CastMemberID.unique();
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();
        final var expectedCreatedAt = Instant.now();

        final var response = new CastMemberListResponse(
          expectedId.toString(),
          expectedName,
          expectedType,
          expectedCreatedAt
        );

        final var result = json.write(response);

        Assertions.assertThat(result)
          .hasJsonPathValue("$.id", expectedId)
          .hasJsonPathValue("$.name", expectedName)
          .hasJsonPathValue("$.type", expectedType)
          .hasJsonPathValue("$.created_at", expectedCreatedAt.toString());
    }
}

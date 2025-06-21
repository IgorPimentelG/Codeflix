package com.fullcycle.admin.catalog.e2e.castmember;

import com.fullcycle.admin.catalog.E2ETest;
import com.fullcycle.admin.catalog.Fixture;
import com.fullcycle.admin.catalog.e2e.MockDsl;
import com.fullcycle.admin.catalog.infrastructure.castmember.models.UpdateCastMemberRequest;
import com.fullcycle.admin.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
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
public class CastMemberE2ETest implements MockDsl {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Container
    private static final MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>("mysql:8.0")
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

    @Override
    public MockMvc mvc() {
        return mvc;
    }

    @Test
    public void asCatalogAdminShouldBeAbleToCreateNewCastMemberWithValidValues() throws Exception {
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var castMemberId = givenCastMember(expectedName, expectedType);

        final var castMember = castMemberRepository.findById(castMemberId.getValue()).get();

        assertEquals(expectedName, castMember.getName());
        assertEquals(expectedType, castMember.getType());
        assertNotNull(castMember.getCreatedAt());
        assertNull(castMember.getUpdatedAt());
    }

    @Test
    public void asCatalogAdminShouldBeAbleTosEEaTreatedErrorByCreatingNewCastMemberWithInvalidValues() throws Exception {
        final String expectedName = null;
        final var expectedType = Fixture.CastMembers.type();
        final var expectedErrorMessage = "Name cannot be null";

        givenCastMemberResult(expectedName, expectedType)
          .andExpect(status().isUnprocessableEntity())
          .andExpect(jsonPath("$.errors", hasSize(1)))
          .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));
    }


    @Test
    public void asCatalogAdminShouldBeAbleToNavigateToAllCastMembers() throws Exception {
        givenCastMember("any name 1", Fixture.CastMembers.type());
        givenCastMember("any name 2", Fixture.CastMembers.type());
        givenCastMember("any name 3", Fixture.CastMembers.type());

        listCastMembers(0, 1)
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.current_page", equalTo(0)))
          .andExpect(jsonPath("$.per_page", equalTo(1)))
          .andExpect(jsonPath("$.total", equalTo(3)))
          .andExpect(jsonPath("$.items", hasSize(1)))
          .andExpect(jsonPath("$.items[0].name", equalTo("any name 3")));

        listCastMembers(1, 1)
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.current_page", equalTo(1)))
          .andExpect(jsonPath("$.per_page", equalTo(1)))
          .andExpect(jsonPath("$.total", equalTo(3)))
          .andExpect(jsonPath("$.items", hasSize(1)))
          .andExpect(jsonPath("$.items[0].name", equalTo("any name 2")));

        listCastMembers(2, 1)
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.current_page", equalTo(2)))
          .andExpect(jsonPath("$.per_page", equalTo(1)))
          .andExpect(jsonPath("$.total", equalTo(3)))
          .andExpect(jsonPath("$.items", hasSize(1)))
          .andExpect(jsonPath("$.items[0].name", equalTo("any name 1")));

        listCastMembers(3, 1)
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.current_page", equalTo(3)))
          .andExpect(jsonPath("$.per_page", equalTo(1)))
          .andExpect(jsonPath("$.total", equalTo(3)))
          .andExpect(jsonPath("$.items", hasSize(0)));
    }

    @Test
    public void asCatalogAdminShouldBeAbleToSearchBetweenAllCastMembers() throws Exception {
        givenCastMember("any name 1", Fixture.CastMembers.type());
        givenCastMember("any name 2", Fixture.CastMembers.type());
        givenCastMember("any name 3", Fixture.CastMembers.type());

        listCastMembers(0, 1, "1")
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.current_page", equalTo(0)))
          .andExpect(jsonPath("$.per_page", equalTo(1)))
          .andExpect(jsonPath("$.total", equalTo(1)))
          .andExpect(jsonPath("$.items", hasSize(1)))
          .andExpect(jsonPath("$.items[0].name", equalTo("any name 1")));
    }

    @Test
    public void asCatalogAdminShouldBeAbleToSortAllCastMemberByNameDesc() throws Exception {
        givenCastMember("any name 1", Fixture.CastMembers.type());
        givenCastMember("any name 2", Fixture.CastMembers.type());
        givenCastMember("any name 3", Fixture.CastMembers.type());

        listCastMembers(0, 3, "desc", "", "name")
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
    public void asCatalogAdminShouldBeAbleToGetCastMemberByItsIdentifier() throws Exception {
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();
        final var castMemberId = givenCastMember(expectedName, expectedType);

        final var castMember = retrieveCastMember(castMemberId);

        assertNotNull(castMember.id());
        assertEquals(expectedName, castMember.name());
        assertEquals(expectedType, castMember.type());
        assertNotNull(castMember.createdAt());
        assertNull(castMember.updatedAt());
    }

    @Test
    public void asCatalogAdminShouldBeAbleToSeeTreatedErrorByGettingNotFoundCastMember() throws Exception {
        final var id = UUID.randomUUID().toString();
        final var expectedErrorMessage = "CastMember with ID %s was not found".formatted(id);

        final var request = MockMvcRequestBuilders.get("/cast-members/" + id).contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void asCatalogAdminShouldBeAbleToUpdateCastMemberByItsIdentifier() throws Exception {
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();
        final var castMemberId = givenCastMember(Fixture.name(), Fixture.CastMembers.type());

        final var input = new UpdateCastMemberRequest(expectedName, expectedType);

        updateCastMember(castMemberId, input).andExpect(status().isOk());

        final var castMember = castMemberRepository.findById(castMemberId.getValue()).get();

        assertEquals(castMemberId.toString(), castMember.getId().toString());
        assertEquals(expectedName, castMember.getName());
        assertEquals(expectedType, castMember.getType());
        assertNotNull(castMember.getCreatedAt());
        assertNotNull(castMember.getUpdatedAt());
    }

    @Test
    public void asCategoryAdminShouldBeAbleToDeleteCastMemberByItsIdentifier() throws Exception {
        final var castMemberId = givenCastMember(Fixture.name(), Fixture.CastMembers.type());
        deleteCastMember(castMemberId).andExpect(status().isNoContent());
        assertFalse(castMemberRepository.existsById(castMemberId.getValue()));
    }

}

package com.fullcycle.admin.catalog.infrastructure.castmember;

import com.fullcycle.admin.catalog.Fixture;
import com.fullcycle.admin.catalog.MySQLGatewayTest;
import com.fullcycle.admin.catalog.domain.castmember.CastMember;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberType;
import com.fullcycle.admin.catalog.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalog.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.fullcycle.admin.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MySQLGatewayTest
public class CastMemberMySQLGatewayTest {

    @Autowired
    private CastMemberMySQLGateway castMemberMySQLGateway;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Test
    public void givenValidCastMember_whenCallsCreate_shouldPersistIt() {
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();
        final var castMember = CastMember.newMember(expectedName, expectedType);
        final var expectedId = castMember.getId();

        assertEquals(0, castMemberRepository.count());

        final var output = castMemberMySQLGateway.create(castMember);

        assertEquals(1, castMemberRepository.count());
        assertNotNull(output.getId());
        assertEquals(expectedName, output.getName());
        assertEquals(expectedType, output.getType());
        assertEquals(castMember.getCreatedAt(), output.getCreatedAt());
        assertEquals(castMember.getUpdatedAt(), output.getUpdatedAt());

        final var persisted = castMemberRepository.findById(expectedId.getValue()).get();

        assertNotNull(persisted.getId());
        assertEquals(expectedName, persisted.getName());
        assertEquals(expectedType, persisted.getType());
        assertEquals(persisted.getCreatedAt(), output.getCreatedAt());
        assertEquals(persisted.getUpdatedAt(), output.getUpdatedAt());
    }

    @Test
    public void givenValidCastMember_whenCallsUpdate_shouldRefreshIt() {
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();
        final var castMember = CastMember.newMember("any name", CastMemberType.DIRECTOR);
        final var expectedId = castMember.getId();

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(castMember));
        assertEquals(1, castMemberRepository.count());

        final var output = castMemberMySQLGateway.update(
          CastMember.with(castMember).update(expectedName, expectedType)
        );

        assertEquals(1, castMemberRepository.count());
        assertNotNull(output.getId());
        assertEquals(expectedName, output.getName());
        assertEquals(expectedType, output.getType());
        assertEquals(castMember.getCreatedAt(), output.getCreatedAt());
        assertNotNull(output.getUpdatedAt());

        final var persisted = castMemberRepository.findById(expectedId.getValue()).get();

        assertNotNull(persisted.getId());
        assertEquals(expectedName, persisted.getName());
        assertEquals(expectedType, persisted.getType());
        assertEquals(persisted.getCreatedAt(), output.getCreatedAt());
        assertNotNull(persisted.getUpdatedAt());
    }

    @Test
    public void givenValidCastMember_whenCallsDeleteById_shouldRemoveIt() {
        final var castMember = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());
        final var expectedId = castMember.getId();

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(castMember));
        assertEquals(1, castMemberRepository.count());

        castMemberMySQLGateway.deleteById(expectedId);

        assertEquals(0, castMemberRepository.count());
    }

    @Test
    public void givenInvalidCastMemberId_whenCallsDeleteById_shouldDoNothing() {
        final var castMember = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(castMember));
        assertEquals(1, castMemberRepository.count());

        castMemberMySQLGateway.deleteById(CastMemberID.unique());

        assertEquals(1, castMemberRepository.count());
    }

    @Test
    public void givenValidCastMember_whenCallsFindById_shouldReturnIt() {
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();
        final var castMember = CastMember.newMember(expectedName, expectedType);
        final var expectedId = castMember.getId();

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(castMember));
        assertEquals(1, castMemberRepository.count());

        final var output = castMemberMySQLGateway.findById(expectedId).get();

        assertNotNull(output.getId());
        assertEquals(expectedName, output.getName());
        assertEquals(expectedType, output.getType());
        assertEquals(castMember.getCreatedAt(), output.getCreatedAt());
        assertEquals(castMember.getUpdatedAt(), output.getUpdatedAt());

        final var persisted = castMemberRepository.findById(expectedId.getValue()).get();

        assertNotNull(persisted.getId());
        assertEquals(expectedName, persisted.getName());
        assertEquals(expectedType, persisted.getType());
        assertEquals(persisted.getCreatedAt(), output.getCreatedAt());
        assertEquals(output.getUpdatedAt(), persisted.getUpdatedAt());
    }

    @Test
    public void givenInvalidId_whenCallsFindById_shouldReturnEmpty() {
        final var castMember = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(castMember));
        assertEquals(1, castMemberRepository.count());

        final var output = castMemberMySQLGateway.findById(CastMemberID.unique());

        assertTrue(output.isEmpty());
    }

    @Test
    public void givenEmptyCastMembers_whenCallsFindAll_shouldReturnEmpty() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var page = castMemberMySQLGateway.findAll(query);

        assertEquals(expectedPage, page.currentPage());
        assertEquals(expectedPerPage, page.perPage());
        assertEquals(expectedTotal, page.total());
        assertEquals(expectedTotal, page.items().size());
    }

    @ParameterizedTest
    @CsvSource({
      "vin,0,10,1,1,Vin Diesel",
      "taran,0,10,1,1,Quentin Tarantino",
      "jas,0,10,1,1,Jason Momoa",
      "har,0,10,1,1,Kit Harington",
      "MAR,0,10,1,1,Martin Scorsese"
    })
    public void givenValidTerm_whenCallsFindAll_shouldReturnFiltered(
      final String expectedTerms,
      final int expectedPage,
      final int expectedPerPage,
      final int expectedItemsCount,
      final long expectedTotal,
      final String expectedName
    ) {
        mockCastMembers();

        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var page = castMemberMySQLGateway.findAll(query);

        assertEquals(expectedPage, page.currentPage());
        assertEquals(expectedPerPage, page.perPage());
        assertEquals(expectedTotal, page.total());
        assertEquals(expectedItemsCount, page.items().size());
        assertEquals(expectedName, page.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
      "name,asc,0,10,5,5,Jason Momoa",
      "name,desc,0,10,5,5,Vin Diesel",
      "createdAt,asc,0,10,5,5,Kit Harington"
    })
    public void givenValidSortAndDirection_whenCallsFindAll_shouldReturnSorted(
      final String expectedSort,
      final String expectedDirection,
      final int expectedPage,
      final int expectedPerPage,
      final int expectedItemsCount,
      final long expectedTotal,
      final String expectedName
    ) {
        mockCastMembers();

        final var expectedTerms = "";

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var page = castMemberMySQLGateway.findAll(query);

        assertEquals(expectedPage, page.currentPage());
        assertEquals(expectedPerPage, page.perPage());
        assertEquals(expectedTotal, page.total());
        assertEquals(expectedItemsCount, page.items().size());
        assertEquals(expectedName, page.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
      "0,2,2,5,Jason Momoa;Kit Harington",
      "1,2,2,5,Martin Scorsese;Quentin Tarantino",
      "2,2,1,5,Vin Diesel"
    })
    public void givenValidPagination_whenCallsFindAll_shouldReturnPaginated(
      final int expectedPage,
      final int expectedPerPage,
      final int expectedItemsCount,
      final long expectedTotal,
      final String expectedNames
    ) {
        mockCastMembers();

        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var page = castMemberMySQLGateway.findAll(query);

        assertEquals(expectedPage, page.currentPage());
        assertEquals(expectedPerPage, page.perPage());
        assertEquals(expectedTotal, page.total());
        assertEquals(expectedItemsCount, page.items().size());

        int index = 0;
        for (final var expectedName : expectedNames.split(";")) {
            assertEquals(expectedName, page.items().get(index).getName());
            index++;
        }
    }

    private void mockCastMembers() {
        castMemberRepository.saveAllAndFlush(
          List.of(
            CastMemberJpaEntity.from(CastMember.newMember("Kit Harington", CastMemberType.ACTOR)),
            CastMemberJpaEntity.from(CastMember.newMember("Vin Diesel", CastMemberType.ACTOR)),
            CastMemberJpaEntity.from(CastMember.newMember("Quentin Tarantino", CastMemberType.DIRECTOR)),
            CastMemberJpaEntity.from(CastMember.newMember("Jason Momoa", CastMemberType.ACTOR)),
            CastMemberJpaEntity.from(CastMember.newMember("Martin Scorsese", CastMemberType.DIRECTOR))
          )
        );
    }

}

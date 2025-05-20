package com.fullcycle.admin.catalog.application.castmember.retrieve.list;

import com.fullcycle.admin.catalog.Fixture;
import com.fullcycle.admin.catalog.IntegrationTest;
import com.fullcycle.admin.catalog.domain.castmember.CastMember;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalog.domain.pagination.Pagination;
import com.fullcycle.admin.catalog.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalog.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.fullcycle.admin.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@IntegrationTest
public class ListCastMembersUseCaseIT {

    @Autowired
    private ListCastMemberUseCase useCase;

    @MockitoSpyBean
    private CastMemberGateway castMemberGateway;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Test
    public void givenValidQuery_whenCallsListCastMember_shouldReturnAll() {
        final var castMembers = List.of(
                CastMember.newMember(Fixture.name(), Fixture.CastMembers.type()),
                CastMember.newMember(Fixture.name(), Fixture.CastMembers.type())
        );

        castMemberRepository.saveAllAndFlush(castMembers.stream().map(CastMemberJpaEntity::from).toList());

        assertEquals(2, castMemberRepository.count());

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = castMembers.stream().map(CastMemberListOutput::from).toList();

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var output = useCase.execute(query);

        assertEquals(expectedPage, output.currentPage());
        assertEquals(expectedPerPage, output.perPage());
        assertEquals(expectedTotal, output.total());
        verify(castMemberGateway, times(1)).findAll(any());
    }

    @Test
    public void givenValidQuery_whenCallsListCastMembersAndIsEmpty_shouldReturn() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "any terms";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var expectedItems = List.<CastMember>of();

        assertEquals(0, castMemberRepository.count());

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var output = useCase.execute(query);

        assertEquals(expectedPage, output.currentPage());
        assertEquals(expectedPerPage, output.perPage());
        assertEquals(expectedTotal, output.total());
        assertEquals(expectedItems, output.items());
        verify(castMemberGateway, times(1)).findAll(any());
    }

    @Test
    public void givenValidQuery_whenCallsListCastMembersAndGatewayThrowsRandomException_shouldException() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "any terms";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var expectedErrorMessage = "Gateway error";

        doThrow(new IllegalStateException(expectedErrorMessage)).when(castMemberGateway).findAll(any());

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var error = assertThrows(IllegalStateException.class, () ->  useCase.execute(query));

        assertEquals(expectedErrorMessage, error.getMessage());
        verify(castMemberGateway, times(1)).findAll(any());
    }
}

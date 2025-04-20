package com.fullcycle.admin.catalog.application.castmember.retrieve.list;

import com.fullcycle.admin.catalog.application.Fixture;
import com.fullcycle.admin.catalog.application.UseCaseTest;
import com.fullcycle.admin.catalog.domain.castmember.CastMember;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalog.domain.pagination.Pagination;
import com.fullcycle.admin.catalog.domain.pagination.SearchQuery;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ListCastMembersUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultListCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenValidQuery_whenCallsListCastMember_shouldReturnAll() {
        final var castMembers = List.of(
                CastMember.newMember(Fixture.name(), Fixture.CastMember.type()),
                CastMember.newMember(Fixture.name(), Fixture.CastMember.type())
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "any terms";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = castMembers.stream().map(CastMemberListOutput::from).toList();
        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, expectedTotal, castMembers);

        when(castMemberGateway.findAll(any())).thenReturn(expectedPagination);

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var output = useCase.execute(query);

        assertEquals(expectedPage, output.currentPage());
        assertEquals(expectedPerPage, output.perPage());
        assertEquals(expectedTotal, output.total());
        assertEquals(expectedItems, output.items());

        verify(castMemberGateway).findAll(eq(query));
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
        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems);

        when(castMemberGateway.findAll(any())).thenReturn(expectedPagination);

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var output = useCase.execute(query);

        assertEquals(expectedPage, output.currentPage());
        assertEquals(expectedPerPage, output.perPage());
        assertEquals(expectedTotal, output.total());
        assertEquals(expectedItems, output.items());

        verify(castMemberGateway).findAll(eq(query));
    }

    @Test
    public void givenValidQuery_whenCallsListCastMembersAndGatewayThrowsRandomException_shouldException() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "any terms";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var expectedErrorMessage = "Gateway error";

        when(castMemberGateway.findAll(any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var error = assertThrows(IllegalStateException.class, () ->  useCase.execute(query));

        assertEquals(expectedErrorMessage, error.getMessage());
        verify(castMemberGateway).findAll(eq(query));
    }
}

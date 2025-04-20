package com.fullcycle.admin.catalog.application.castmember.retrieve.get;

import com.fullcycle.admin.catalog.application.Fixture;
import com.fullcycle.admin.catalog.application.UseCaseTest;
import com.fullcycle.admin.catalog.domain.castmember.CastMember;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalog.domain.errors.NotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetCastMemberUesCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenValidId_whenCallsGetCastMember_shouldReturnIt() {
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();

        final var castMember = CastMember.newMember(expectedName, expectedType);
        final var expectedId = castMember.getId();

        when(castMemberGateway.findById(any())).thenReturn(Optional.of(castMember));

        final var output = useCase.execute(expectedId.getValue());

        assertNotNull(output);
        assertEquals(expectedId.getValue(), output.id());
        assertEquals(expectedName, output.name());
        assertEquals(expectedType, output.type());
        assertEquals(castMember.getCreatedAt(), output.createdAt());
        assertEquals(castMember.getUpdatedAt(), output.updatedAt());

        verify(castMemberGateway).findById(eq(expectedId));
    }

    @Test
    public void givenInvalidId_whenCallsGetCastMember_shouldReturnNotFoundException() {
        final var expectedId = CastMemberID.unique();
        final var expectedErrorMessage = "CastMember with ID %s was not found".formatted(expectedId.getValue());

        when(castMemberGateway.findById(any())).thenReturn(Optional.empty());

        final var error = assertThrows(NotFoundException.class, () -> useCase.execute(expectedId.getValue()));

        assertEquals(expectedErrorMessage, error.getMessage());
        verify(castMemberGateway).findById(eq(expectedId));
    }
}

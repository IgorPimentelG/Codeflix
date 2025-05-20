package com.fullcycle.admin.catalog.application.castmember.retrieve.get;

import com.fullcycle.admin.catalog.Fixture;
import com.fullcycle.admin.catalog.IntegrationTest;
import com.fullcycle.admin.catalog.domain.castmember.CastMember;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalog.domain.errors.NotFoundException;
import com.fullcycle.admin.catalog.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.fullcycle.admin.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@IntegrationTest
public class GetCastMemberUesCaseIT {

    @Autowired
    private GetCastMemberUseCase useCase;

    @MockitoSpyBean
    private CastMemberGateway castMemberGateway;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Test
    public void givenValidId_whenCallsGetCastMember_shouldReturnIt() {
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var castMember = CastMember.newMember(expectedName, expectedType);
        final var expectedId = castMember.getId();

        castMemberRepository.save(CastMemberJpaEntity.from(castMember));

        final var output = useCase.execute(expectedId.getValue());

        assertNotNull(output);
        assertEquals(expectedId.getValue(), output.id());
        assertEquals(expectedName, output.name());
        assertEquals(expectedType, output.type());
        assertNotNull(output.createdAt());
        assertNull(output.updatedAt());
        verify(castMemberGateway, times(1)).findById(any());
    }

    @Test
    public void givenInvalidId_whenCallsGetCastMember_shouldReturnNotFoundException() {
        final var expectedId = CastMemberID.unique();
        final var expectedErrorMessage = "CastMember with ID %s was not found".formatted(expectedId.getValue());

        final var error = assertThrows(NotFoundException.class, () -> useCase.execute(expectedId.getValue()));
        assertEquals(expectedErrorMessage, error.getMessage());
        verify(castMemberGateway, times(1)).findById(any());
    }
}

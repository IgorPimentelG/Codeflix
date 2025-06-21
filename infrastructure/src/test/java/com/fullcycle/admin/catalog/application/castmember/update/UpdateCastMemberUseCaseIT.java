package com.fullcycle.admin.catalog.application.castmember.update;

import com.fullcycle.admin.catalog.Fixture;
import com.fullcycle.admin.catalog.IntegrationTest;
import com.fullcycle.admin.catalog.domain.castmember.CastMember;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberType;
import com.fullcycle.admin.catalog.domain.errors.NotFoundException;
import com.fullcycle.admin.catalog.domain.errors.NotificationException;
import com.fullcycle.admin.catalog.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.fullcycle.admin.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@IntegrationTest
public class UpdateCastMemberUseCaseIT {

    @Autowired
    private DefaultUpdateCastMemberUseCase useCase;

    @MockitoSpyBean
    private CastMemberGateway castMemberGateway;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Test
    public void givenValidCommand_whenCallsUpdateCastMember_shouldReturnItsIdentifier() {
        final var castMember = CastMember.newMember("any name", CastMemberType.DIRECTOR);
        castMemberRepository.save(CastMemberJpaEntity.from(castMember));

        assertEquals(1, castMemberRepository.count());

        final var expectedId = castMember.getId();
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var command = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);
        final var output = useCase.execute(command);

        final var persisted = castMemberRepository.findById(output.id()).get();

        assertEquals(expectedName, persisted.getName());
        assertEquals(expectedType, persisted.getType());
        assertNotNull(persisted.getCreatedAt());
        assertNotNull(persisted.getUpdatedAt());
        verify(castMemberGateway, times(1)).update(any());
        verify(castMemberGateway, times(1)).findById(any());
    }

    @Test
    public void givenInvalidName_whenCallsUpdateCastMember_shouldThrowsNotification() {
        final var castMember = CastMember.newMember("any name", CastMemberType.DIRECTOR);
        castMemberRepository.save(CastMemberJpaEntity.from(castMember));

        assertEquals(1, castMemberRepository.count());

        final var expectedId = castMember.getId();
        final String expectedName = null;
        final var expectedType = Fixture.CastMembers.type();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name cannot be null";

        final var command = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);

        final var error = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertNotNull(error);
        assertEquals(expectedErrorCount, error.getErrors().size());
        assertEquals(expectedErrorMessage, error.getErrors().get(0).message());
        verify(castMemberGateway, times(0)).update(any());
        verify(castMemberGateway, times(1)).findById(any());
    }

    @Test
    public void givenInvalidType_whenCallsUpdateCastMember_shouldThrowsNotification() {
        final var castMember = CastMember.newMember("any name", CastMemberType.DIRECTOR);
        castMemberRepository.save(CastMemberJpaEntity.from(castMember));

        assertEquals(1, castMemberRepository.count());

        final var expectedId = castMember.getId();
        final var expectedName = Fixture.name();
        final CastMemberType expectedType = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Type cannot be null";

        final var command = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);
        final var error = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertNotNull(error);
        assertEquals(expectedErrorCount, error.getErrors().size());
        assertEquals(expectedErrorMessage, error.getErrors().get(0).message());
        verify(castMemberGateway, times(0)).update(any());
        verify(castMemberGateway, times(1)).findById(any());
    }

    @Test
    public void givenInvalidId_whenCallsUpdateCastMember_shouldThrowsNotification() {
        final var expectedId = CastMemberID.unique();
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();
        final var expectedErrorMessage = "CastMember with ID %s was not found".formatted(expectedId.getValue());

        final var command = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);

        final var error = assertThrows(NotFoundException.class, () -> useCase.execute(command));

        assertNotNull(error);
        assertEquals(expectedErrorMessage,  error.getMessage());
        verify(castMemberGateway, times(0)).update(any());
        verify(castMemberGateway, times(1)).findById(any());
    }
}

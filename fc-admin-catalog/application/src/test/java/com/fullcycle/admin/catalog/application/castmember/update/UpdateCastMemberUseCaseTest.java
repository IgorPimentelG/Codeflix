package com.fullcycle.admin.catalog.application.castmember.update;

import com.fullcycle.admin.catalog.application.Fixture;
import com.fullcycle.admin.catalog.application.UseCaseTest;
import com.fullcycle.admin.catalog.domain.castmember.CastMember;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberType;
import com.fullcycle.admin.catalog.domain.errors.NotFoundException;
import com.fullcycle.admin.catalog.domain.errors.NotificationException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class UpdateCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;


    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenValidCommand_whenCallsUpdateCastMember_shouldReturnItsIdentifier() {
        final var castMember = CastMember.newMember("any name", CastMemberType.DIRECTOR);

        final var expectedId = castMember.getId();
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();

        final var command = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);

        when(castMemberGateway.findById(any())).thenReturn(Optional.of(castMember));
        when(castMemberGateway.update(any())).thenAnswer(returnsFirstArg());

        final var output = useCase.execute(command);

        assertNotNull(output);
        assertEquals(expectedId.getValue(), output.id());
        verify(castMemberGateway).findById(eq(expectedId));
        verify(castMemberGateway).update(argThat(updated ->
          Objects.equals(expectedId, updated.getId()) &&
          Objects.equals(expectedName, updated.getName()) &&
          Objects.equals(expectedType, updated.getType()) &&
          Objects.nonNull(updated.getCreatedAt()) &&
          Objects.nonNull(updated.getUpdatedAt())
        ));
    }

    @Test
    public void givenInvalidName_whenCallsUpdateCastMember_shouldThrowsNotification() {
        final var castMember = CastMember.newMember("any name", CastMemberType.DIRECTOR);

        final var expectedId = castMember.getId();
        final String expectedName = null;
        final var expectedType = Fixture.CastMember.type();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name cannot be null";

        final var command = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);

        when(castMemberGateway.findById(any())).thenReturn(Optional.of(castMember));

        final var error = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertNotNull(error);
        assertEquals(expectedErrorCount, error.getErrors().size());
        assertEquals(expectedErrorMessage, error.getErrors().get(0).message());
        verify(castMemberGateway).findById(eq(expectedId));
        verify(castMemberGateway, times(0)).update(any());
    }

    @Test
    public void givenInvalidType_whenCallsUpdateCastMember_shouldThrowsNotification() {
        final var castMember = CastMember.newMember("any name", CastMemberType.DIRECTOR);

        final var expectedId = castMember.getId();
        final var expectedName = Fixture.name();
        final CastMemberType expectedType = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Type cannot be null";

        final var command = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);

        when(castMemberGateway.findById(any())).thenReturn(Optional.of(castMember));

        final var error = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertNotNull(error);
        assertEquals(expectedErrorCount, error.getErrors().size());
        assertEquals(expectedErrorMessage, error.getErrors().get(0).message());
        verify(castMemberGateway).findById(eq(expectedId));
        verify(castMemberGateway, times(0)).update(any());
    }

    @Test
    public void givenInvalidId_whenCallsUpdateCastMember_shouldThrowsNotification() {
        final var expectedId = CastMemberID.from(UUID.randomUUID());
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();
        final var expectedErrorMessage = "CastMember with ID %s was not found".formatted(expectedId.getValue());

        final var command = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);

        when(castMemberGateway.findById(any())).thenReturn(Optional.empty());

        final var error = assertThrows(NotFoundException.class, () -> useCase.execute(command));

        assertNotNull(error);
        assertEquals(expectedErrorMessage,  error.getMessage());
        verify(castMemberGateway).findById(eq(expectedId));
        verify(castMemberGateway, times(0)).update(any());
    }
}

package com.fullcycle.admin.catalog.application.castmember.create;

import com.fullcycle.admin.catalog.application.Fixture;
import com.fullcycle.admin.catalog.application.UseCaseTest;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalog.domain.errors.NotificationException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class CreateCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenValidCommand_whenCallsCreateMember_shouldReturnIt() {
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();
        final var command = CreateCastMemberCommand.with(expectedName, expectedType);

        when(castMemberGateway.create(any())).thenAnswer(returnsFirstArg());

        final var output = useCase.execute(command);

        assertNotNull(output);
        assertNotNull(output.id());

        verify(castMemberGateway).create(argThat(castMember ->
            Objects.nonNull(castMember.getId()) &&
            Objects.equals(expectedName,  castMember.getName()) &&
            Objects.equals(expectedType, castMember.getType()) &&
            Objects.nonNull(castMember.getCreatedAt()) &&
            Objects.isNull(castMember.getUpdatedAt())
        ));
    }

    @Test
    public void givenInvalidName_whenCallsCreateMember_shouldReturnError() {
        final String expectedName = null;
        final var expectedType = Fixture.CastMembers.type();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name cannot be null";

        final var command = CreateCastMemberCommand.with(expectedName, expectedType);

        final var output = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertNotNull(output);
        assertEquals(expectedErrorCount, output.getErrors().size());
        assertEquals(expectedErrorMessage, output.getErrors().get(0).message());

        verify(castMemberGateway, times(0)).create(any());
    }
}

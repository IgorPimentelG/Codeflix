package com.fullcycle.admin.catalog.application.castmember.create;

import com.fullcycle.admin.catalog.Fixture;
import com.fullcycle.admin.catalog.IntegrationTest;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalog.domain.errors.NotificationException;
import com.fullcycle.admin.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@IntegrationTest
public class CreateCastMemberUseCaseIT {

    @Autowired
    private CreateCastMemberUseCase useCase;

    @MockitoSpyBean
    private CastMemberGateway castMemberGateway;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Test
    public void givenValidCommand_whenCallsCreateMember_shouldReturnIt() {
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();
        final var command = CreateCastMemberCommand.with(expectedName, expectedType);

        final var output = useCase.execute(command);

        assertNotNull(output);
        assertNotNull(output.id());

        final var persisted = castMemberRepository.findById(output.id()).get();

        assertEquals(expectedName, persisted.getName());
        assertEquals(expectedType, persisted.getType());
        assertNotNull(persisted.getCreatedAt());
        assertNull(persisted.getUpdatedAt());
        verify(castMemberGateway, times(1)).create(any());
    }

    @Test
    public void givenInvalidName_whenCallsCreateMember_shouldReturnError() {
        final String expectedName = null;
        final var expectedType = Fixture.CastMember.type();
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

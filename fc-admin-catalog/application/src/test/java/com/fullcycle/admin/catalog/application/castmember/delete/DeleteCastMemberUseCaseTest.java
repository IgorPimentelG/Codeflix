package com.fullcycle.admin.catalog.application.castmember.delete;

import com.fullcycle.admin.catalog.application.Fixture;
import com.fullcycle.admin.catalog.application.UseCaseTest;
import com.fullcycle.admin.catalog.domain.castmember.CastMember;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DeleteCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenValidId_whenCallsDeleteCastMember_shouldDeleteIt() {
        final var castMember = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());
        final var expectedId = castMember.getId();

        doNothing().when(castMemberGateway).deleteById(any());
        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        verify(castMemberGateway).deleteById(eq(expectedId));
    }

    @Test
    public void givenInvalidId_whenCallsDeleteCastMember_shouldBeOk() {
        final var expectedId = CastMemberID.unique();

        doNothing().when(castMemberGateway).deleteById(any());
        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        verify(castMemberGateway).deleteById(eq(expectedId));
    }

    @Test
    public void givenValidId_whenCallsDeleteCastMemberAndGatewayThrowsException_shouldReceiveException() {
        final var castMember = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());
        final var expectedId = castMember.getId();

        doThrow(new IllegalStateException("Gateway error")).when(castMemberGateway).deleteById(any());
        assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));
        verify(castMemberGateway).deleteById(eq(expectedId));
    }
}

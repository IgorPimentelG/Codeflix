package com.fullcycle.admin.catalog.application.castmember.delete;

import com.fullcycle.admin.catalog.Fixture;
import com.fullcycle.admin.catalog.IntegrationTest;
import com.fullcycle.admin.catalog.domain.castmember.CastMember;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalog.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.fullcycle.admin.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@IntegrationTest
public class DeleteCastMemberUseCaseIT {

    @Autowired
    private DeleteCastMemberUseCase useCase;

    @MockitoSpyBean
    private CastMemberGateway castMemberGateway;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Test
    public void givenValidId_whenCallsDeleteCastMember_shouldDeleteIt() {
        final var castMember = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());
        final var expectedId = castMember.getId();

        castMemberRepository.save(CastMemberJpaEntity.from(castMember));

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        verify(castMemberGateway, times(1)).deleteById(any());
        assertEquals(0, castMemberRepository.count());
    }

    @Test
    public void givenInvalidId_whenCallsDeleteCastMember_shouldBeOk() {
        final var expectedId = CastMemberID.unique();
        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        verify(castMemberGateway, times(1)).deleteById(any());
    }

    @Test
    public void givenValidId_whenCallsDeleteCastMemberAndGatewayThrowsException_shouldReceiveException() {
        final var castMember = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());
        final var expectedId = castMember.getId();

        castMemberRepository.save(CastMemberJpaEntity.from(castMember));

        assertEquals(1, castMemberRepository.count());
        doThrow(new IllegalStateException("Gateway error")).when(castMemberGateway).deleteById(any());
        assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));
        verify(castMemberGateway, times(1)).deleteById(any());
        assertEquals(1, castMemberRepository.count());
    }
}

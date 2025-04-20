package com.fullcycle.admin.catalog.application.castmember.retrieve.get;

import com.fullcycle.admin.catalog.domain.castmember.CastMember;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalog.domain.errors.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public final class DefaultGetCastMemberUseCase extends GetCastMemberUseCase {

    private final CastMemberGateway castMemberGateway;

    @Override
    public CastMemberOutput execute(final UUID id) {
        final var castMemberId = CastMemberID.from(id);
        return castMemberGateway.findById(castMemberId)
            .map(CastMemberOutput::from)
            .orElseThrow(() -> NotFoundException.with(CastMember.class, castMemberId));
    }
}

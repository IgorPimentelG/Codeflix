package com.fullcycle.admin.catalog.application.castmember.delete;

import com.fullcycle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberID;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public final class DefaultDeleteCastMemberUseCase extends DeleteCastMemberUseCase {

    private final CastMemberGateway castMemberGateway;

    @Override
    public void execute(final UUID id) {
        castMemberGateway.deleteById(CastMemberID.from(id));
    }
}

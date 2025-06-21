package com.fullcycle.admin.catalog.application.castmember.create;

import com.fullcycle.admin.catalog.domain.castmember.CastMember;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberID;

import java.util.UUID;

public record CreateCastMemberOutput(UUID id) {

    public static CreateCastMemberOutput from(final CastMember castMember) {
        return new CreateCastMemberOutput(castMember.getId().getValue());
    }

    public static CreateCastMemberOutput from(final CastMemberID id) {
        return new CreateCastMemberOutput(id.getValue());
    }
}

package com.fullcycle.admin.catalog.application.castmember.update;

import com.fullcycle.admin.catalog.domain.castmember.CastMember;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberID;

import java.util.UUID;

public record UpdateCastMemberOutput(UUID id) {

    public static UpdateCastMemberOutput from(final CastMember castMember) {
        return new UpdateCastMemberOutput(castMember.getId().getValue());
    }

    public static UpdateCastMemberOutput from(final CastMemberID id) {
        return new UpdateCastMemberOutput(id.getValue());
    }
}

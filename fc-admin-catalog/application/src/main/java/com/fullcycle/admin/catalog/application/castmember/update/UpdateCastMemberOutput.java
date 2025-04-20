package com.fullcycle.admin.catalog.application.castmember.update;

import com.fullcycle.admin.catalog.domain.castmember.CastMember;

import java.util.UUID;

public record UpdateCastMemberOutput(UUID id) {

    public static UpdateCastMemberOutput from(final CastMember castMember) {
        return new UpdateCastMemberOutput(castMember.getId().getValue());
    }
}

package com.fullcycle.admin.catalog.application.castmember.update;

import com.fullcycle.admin.catalog.domain.castmember.CastMemberType;

import java.util.UUID;

public record UpdateCastMemberCommand(UUID id, String name, CastMemberType type) {

    public static UpdateCastMemberCommand with(final UUID id, final String name, final CastMemberType type) {
        return new UpdateCastMemberCommand(id, name, type);
    }
}

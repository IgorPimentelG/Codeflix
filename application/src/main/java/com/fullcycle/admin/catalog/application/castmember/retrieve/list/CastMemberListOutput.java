package com.fullcycle.admin.catalog.application.castmember.retrieve.list;

import com.fullcycle.admin.catalog.domain.castmember.CastMember;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberType;

import java.time.Instant;
import java.util.UUID;

public record CastMemberListOutput(
  UUID id,
  String name,
  CastMemberType type,
  Instant createdAt
) {

    public static CastMemberListOutput from(final CastMember castMember) {
        return new CastMemberListOutput(
            castMember.getId().getValue(),
            castMember.getName(),
            castMember.getType(),
            castMember.getCreatedAt()
        );
    }
}

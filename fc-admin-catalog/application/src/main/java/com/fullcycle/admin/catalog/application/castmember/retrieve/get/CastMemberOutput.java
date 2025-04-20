package com.fullcycle.admin.catalog.application.castmember.retrieve.get;

import com.fullcycle.admin.catalog.domain.castmember.CastMember;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberType;

import java.time.Instant;
import java.util.UUID;

public record CastMemberOutput(
  UUID id,
  String name,
  CastMemberType type,
  Instant createdAt,
  Instant updatedAt
) {

    public static CastMemberOutput from(final CastMember castMember) {
        return new CastMemberOutput(
          castMember.getId().getValue(),
          castMember.getName(),
          castMember.getType(),
          castMember.getCreatedAt(),
          castMember.getUpdatedAt()
        );
    }
}

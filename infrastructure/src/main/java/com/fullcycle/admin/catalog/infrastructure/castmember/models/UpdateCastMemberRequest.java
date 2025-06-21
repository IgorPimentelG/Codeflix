package com.fullcycle.admin.catalog.infrastructure.castmember.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberType;

public record UpdateCastMemberRequest(
  @JsonProperty("name") String name,
  @JsonProperty("type") CastMemberType type
) {}

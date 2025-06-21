package com.fullcycle.admin.catalog.infrastructure.genre.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record GenreListResponse(
  @JsonProperty("id") String id,
  @JsonProperty("name") String name,
  @JsonProperty("is_active") Boolean isActive,
  @JsonProperty("created_at") Instant createdAt,
  @JsonProperty("deleted_at") Instant deletedAt
) {}

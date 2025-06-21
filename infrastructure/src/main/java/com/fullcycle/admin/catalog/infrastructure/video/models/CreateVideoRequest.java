package com.fullcycle.admin.catalog.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public record CreateVideoRequest(
  @JsonProperty("title") String title,
  @JsonProperty("description") String description,
  @JsonProperty("year_launched") Integer launchedAt,
  @JsonProperty("duration") Double duration,
  @JsonProperty("opened") Boolean opened,
  @JsonProperty("published") Boolean published,
  @JsonProperty("rating") String rating,
  @JsonProperty("categories_id") Set<String> categories,
  @JsonProperty("genres_id") Set<String> genres,
  @JsonProperty("cast_members_id") Set<String> castMembers
) {}

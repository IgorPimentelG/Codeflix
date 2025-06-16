package com.fullcycle.admin.catalog.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Set;

public record VideoResponse(
  @JsonProperty("id") String id,
  @JsonProperty("title") String title,
  @JsonProperty("description") String description,
  @JsonProperty("year_launched") Integer yearLaunched,
  @JsonProperty("duration") Double duration,
  @JsonProperty("opened") Boolean opened,
  @JsonProperty("published") Boolean published,
  @JsonProperty("rating") String rating,
  @JsonProperty("created_at") Instant createdAt,
  @JsonProperty("updated_at") Instant updatedAt,
  @JsonProperty("banner") ImageMediaResponse banner,
  @JsonProperty("thumbnail") ImageMediaResponse thumbnail,
  @JsonProperty("thumbnail_half") ImageMediaResponse thumbnailHalf,
  @JsonProperty("video") AudioVideoMediaResponse video,
  @JsonProperty("trailer") AudioVideoMediaResponse trailer,
  @JsonProperty("categories_id") Set<String> categories,
  @JsonProperty("genres_id") Set<String> genres,
  @JsonProperty("cast_members_id") Set<String> castMembers
) {}

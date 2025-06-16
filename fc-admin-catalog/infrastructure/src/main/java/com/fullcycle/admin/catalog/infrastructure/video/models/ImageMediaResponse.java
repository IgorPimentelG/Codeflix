package com.fullcycle.admin.catalog.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ImageMediaResponse(
  @JsonProperty("checksum") String checksum,
  @JsonProperty("name") String name,
  @JsonProperty("location") String rawLocation
) {}

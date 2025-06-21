package com.fullcycle.admin.catalog.infrastructure.genre.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record UpdateGenreRequest(
  @JsonProperty("name") String name,
  @JsonProperty("categories_id") List<String> categories,
  @JsonProperty("is_active") Boolean isActive
) {
    public boolean active() {
        return isActive != null ? isActive : true;
    }

    public List<String> categories() {
        return categories == null ? List.of() : categories;
    }
}

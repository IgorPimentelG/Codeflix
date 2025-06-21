package com.fullcycle.admin.catalog.application.genre.retrieve.get;

import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.genre.Genre;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record GenreOutput(
  UUID id,
  String name,
  boolean isActive,
  List<String> categories,
  Instant createdAt,
  Instant updatedAt,
  Instant deletedAt
) {
    public static GenreOutput from(final Genre genre) {
        return new GenreOutput(
          genre.getId().getValue(),
          genre.getName(),
          genre.isActive(),
          genre.getCategories()
            .stream()
            .map(CategoryID::getValue)
            .map(UUID::toString)
            .toList(),
          genre.getCreatedAt(),
          genre.getUpdatedAt(),
          genre.getDeletedAt()
        );
    }
}

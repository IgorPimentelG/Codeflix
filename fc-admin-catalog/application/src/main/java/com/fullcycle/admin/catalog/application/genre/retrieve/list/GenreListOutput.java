package com.fullcycle.admin.catalog.application.genre.retrieve.list;

import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.genre.Genre;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record GenreListOutput(
  String name,
  boolean isActive,
  List<String> categories,
  Instant createdAt,
  Instant deletedAt
) {
    public static GenreListOutput from(final Genre genre) {
        return new GenreListOutput(
          genre.getName(),
          genre.isActive(),
          genre.getCategories()
            .stream()
            .map(CategoryID::getValue)
            .map(UUID::toString)
            .toList(),
          genre.getCreatedAt(),
          genre.getDeletedAt()
        );
    }
}

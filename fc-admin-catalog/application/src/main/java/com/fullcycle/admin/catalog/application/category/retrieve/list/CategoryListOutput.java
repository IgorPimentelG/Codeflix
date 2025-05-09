package com.fullcycle.admin.catalog.application.category.retrieve.list;

import com.fullcycle.admin.catalog.domain.category.Category;
import com.fullcycle.admin.catalog.domain.category.CategoryID;

import java.time.Instant;

public record CategoryListOutput(
  CategoryID id,
  String name,
  String description,
  boolean isActive,
  Instant createdAt,
  Instant updatedAt,
  Instant deletedAt
) {
    public static CategoryListOutput from(Category category) {
        return new CategoryListOutput(
          category.getId(),
          category.getName(),
          category.getDescription(),
          category.isActive(),
          category.getCreatedAt(),
          category.getUpdatedAt(),
          category.getDeletedAt()
        );
    }
}

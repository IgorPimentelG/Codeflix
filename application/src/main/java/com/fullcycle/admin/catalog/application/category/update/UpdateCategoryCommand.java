package com.fullcycle.admin.catalog.application.category.update;

import com.fullcycle.admin.catalog.domain.category.CategoryID;

import java.util.UUID;

public record UpdateCategoryCommand(
  CategoryID id,
  String name,
  String description,
  boolean isActive
) {
    public static UpdateCategoryCommand with(final CategoryID id, final String name, final String description, final boolean isActive) {
        return new UpdateCategoryCommand(id, name, description, isActive);
    }
}

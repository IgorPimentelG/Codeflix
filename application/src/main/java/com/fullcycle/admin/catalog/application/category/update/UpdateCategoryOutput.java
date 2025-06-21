package com.fullcycle.admin.catalog.application.category.update;

import com.fullcycle.admin.catalog.domain.category.Category;

import java.util.UUID;

public record UpdateCategoryOutput(UUID categoryID) {
    public static UpdateCategoryOutput from(final Category category) {
        return new UpdateCategoryOutput(category.getId().getValue());
    }
}

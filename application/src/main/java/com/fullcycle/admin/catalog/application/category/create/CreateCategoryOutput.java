package com.fullcycle.admin.catalog.application.category.create;

import com.fullcycle.admin.catalog.domain.category.Category;
import com.fullcycle.admin.catalog.domain.category.CategoryID;

import java.util.UUID;

public record CreateCategoryOutput(UUID categoryID) {
    public static CreateCategoryOutput from(final Category category) {
        return new CreateCategoryOutput(category.getId().getValue());
    }

    public static CreateCategoryOutput from(final CategoryID id) {
        return new CreateCategoryOutput(id.getValue());
    }
}

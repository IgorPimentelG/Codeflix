package com.fullcycle.admin.catalog.infrastructure.category.presenters;

import com.fullcycle.admin.catalog.application.category.retrieve.get.CategoryOutput;
import com.fullcycle.admin.catalog.application.category.retrieve.list.CategoryListOutput;
import com.fullcycle.admin.catalog.infrastructure.category.models.CategoryResponse;
import com.fullcycle.admin.catalog.infrastructure.category.models.CategoryListResponse;

public interface CategoryApiPresenter {

    static CategoryResponse present(final CategoryOutput output) {
        return new CategoryResponse(
          output.id().toString(),
          output.name(),
          output.description(),
          output.isActive(),
          output.createdAt(),
          output.updatedAt(),
          output.deletedAt()
        );
    }

    static CategoryListResponse present(final CategoryListOutput output) {
        return new CategoryListResponse(
          output.id().toString(),
          output.name(),
          output.description(),
          output.isActive(),
          output.createdAt()
        );
    }
}

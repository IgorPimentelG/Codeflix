package com.fullcycle.admin.catalog.application.category.retrieve.get;

import com.fullcycle.admin.catalog.domain.category.Category;
import com.fullcycle.admin.catalog.domain.category.CategoryGateway;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.errors.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.UUID;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class DefaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase {

    private final CategoryGateway categoryGateway;

    @Override
    public CategoryOutput execute(UUID id) {
        final var categoryId = CategoryID.from(id);
        return categoryGateway.findById(categoryId)
          .map(CategoryOutput::from)
          .orElseThrow(notFound(categoryId));
    }

    private Supplier<NotFoundException> notFound(CategoryID categoryId) {
        return () -> NotFoundException.with(Category.class, categoryId);
    }
}

package com.fullcycle.admin.catalog.application.category.retrieve.get;

import com.fullcycle.admin.catalog.domain.category.CategoryGateway;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.errors.DomainException;
import com.fullcycle.admin.catalog.domain.validation.Error;
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

    private Supplier<? extends RuntimeException> notFound(CategoryID categoryId) {
        return () -> DomainException.with(
          new Error("Category with ID %s was not found".formatted(categoryId.getValue()))
        );
    }

}

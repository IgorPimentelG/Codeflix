package com.fullcycle.admin.catalog.application.category.delete;

import com.fullcycle.admin.catalog.domain.category.CategoryGateway;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class DefaultDeleteCategoryUseCase extends DeleteCategoryUseCase {

    private final CategoryGateway categoryGateway;

    @Override
    public void execute(UUID id) {
        categoryGateway.delete(CategoryID.from(id));
    }
}

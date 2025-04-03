package com.fullcycle.admin.catalog.infrastructure.api.controllers;

import com.fullcycle.admin.catalog.application.category.create.CreateCategoryCommand;
import com.fullcycle.admin.catalog.application.category.create.CreateCategoryOutput;
import com.fullcycle.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.fullcycle.admin.catalog.application.category.delete.DeleteCategoryUseCase;
import com.fullcycle.admin.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.fullcycle.admin.catalog.application.category.retrieve.list.ListCategoriesUseCase;
import com.fullcycle.admin.catalog.application.category.update.UpdateCategoryCommand;
import com.fullcycle.admin.catalog.application.category.update.UpdateCategoryOutput;
import com.fullcycle.admin.catalog.application.category.update.UpdateCategoryUseCase;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalog.domain.pagination.Pagination;
import com.fullcycle.admin.catalog.domain.validation.handler.Notification;
import com.fullcycle.admin.catalog.infrastructure.api.CategoryAPI;
import com.fullcycle.admin.catalog.infrastructure.category.models.CategoryListResponse;
import com.fullcycle.admin.catalog.infrastructure.category.models.CategoryResponse;
import com.fullcycle.admin.catalog.infrastructure.category.models.CreateCategoryRequest;
import com.fullcycle.admin.catalog.infrastructure.category.models.UpdateCategoryRequest;
import com.fullcycle.admin.catalog.infrastructure.category.presenters.CategoryApiPresenter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;
import java.util.function.Function;

@RestController
@RequiredArgsConstructor
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final ListCategoriesUseCase listCategoriesUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;

    @Override
    public ResponseEntity<?> createCategory(CreateCategoryRequest input) {
        final var command = CreateCategoryCommand.with(
          input.name(),
          input.description(),
          input.active() != null ? input.active() : true
        );

        final Function<Notification, ResponseEntity<?>> error = ResponseEntity.unprocessableEntity()::body;
        final Function<CreateCategoryOutput, ResponseEntity<?>> success = (output) -> ResponseEntity.created(
          URI.create(String.format("/categories/%s", output.categoryID()))
        ).body(output);

        return createCategoryUseCase.execute(command).fold(error, success);
    }

    @Override
    public Pagination<CategoryListResponse> listCategories(
      final String search,
      final int page,
      final int perPage,
      final String sort,
      final String direction
    ) {
        final var query = new SearchQuery(page, perPage, search, sort, direction);
        return listCategoriesUseCase.execute(query).map(CategoryApiPresenter::present);
    }

    @Override
    public CategoryResponse getById(UUID id) {
        return CategoryApiPresenter.present(getCategoryByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> update(final UUID id, final UpdateCategoryRequest input) {
        final var command = UpdateCategoryCommand.with(
          CategoryID.from(id),
          input.name(),
          input.description(),
          input.active() != null ? input.active() : true
        );

        final Function<Notification, ResponseEntity<?>> error = ResponseEntity.unprocessableEntity()::body;
        final Function<UpdateCategoryOutput, ResponseEntity<?>> success = ResponseEntity::ok;

        return updateCategoryUseCase.execute(command).fold(error, success);
    }

    @Override
    public void delete(final UUID id) {
        deleteCategoryUseCase.execute(id);
    }
}

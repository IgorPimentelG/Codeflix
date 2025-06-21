package com.fullcycle.admin.catalog.application.category.update;

import com.fullcycle.admin.catalog.domain.category.Category;
import com.fullcycle.admin.catalog.domain.category.CategoryGateway;
import com.fullcycle.admin.catalog.domain.errors.DomainException;
import com.fullcycle.admin.catalog.domain.validation.Error;
import com.fullcycle.admin.catalog.domain.validation.handler.Notification;
import io.vavr.API;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    @Override
    public Either<Notification, UpdateCategoryOutput> execute(UpdateCategoryCommand command) {
        final Category category = categoryGateway.findById(command.id())
          .orElseThrow(() -> DomainException.with(new Error("Category with ID %s was not found".formatted(command.id().getValue()))));

        final var notification = Notification.create();
        category.update(command.name(), command.description(), command.isActive())
          .validate(notification);

        return notification.hasError() ? API.Left(notification) : update(category);
    }

    private Either<Notification, UpdateCategoryOutput> update(Category category) {
        return API.Try(() -> categoryGateway.update(category))
          .toEither()
          .bimap(Notification::create, UpdateCategoryOutput::from);
    }
}

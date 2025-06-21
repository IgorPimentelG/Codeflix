package com.fullcycle.admin.catalog.application.category.create;

import com.fullcycle.admin.catalog.domain.category.Category;
import com.fullcycle.admin.catalog.domain.category.CategoryGateway;
import com.fullcycle.admin.catalog.domain.validation.handler.Notification;
import io.vavr.API;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    @Override
    public Either<Notification, CreateCategoryOutput> execute(CreateCategoryCommand command) {
        final var notification = Notification.create();
        final var category = Category.newCategory(command.name(), command.description(), command.isActive());
        category.validate(notification);
        return notification.hasError() ? API.Left(notification) : create(category);
    }

    private Either<Notification, CreateCategoryOutput> create(final Category category) {
        return API.Try(() -> categoryGateway.create(category))
          .toEither()
          .bimap(Notification::create, CreateCategoryOutput::from);
    }
}

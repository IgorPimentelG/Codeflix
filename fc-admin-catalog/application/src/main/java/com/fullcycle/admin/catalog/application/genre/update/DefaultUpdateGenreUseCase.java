package com.fullcycle.admin.catalog.application.genre.update;

import com.fullcycle.admin.catalog.domain.category.CategoryGateway;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.errors.DomainException;
import com.fullcycle.admin.catalog.domain.errors.NotFoundException;
import com.fullcycle.admin.catalog.domain.errors.NotificationException;
import com.fullcycle.admin.catalog.domain.genre.Genre;
import com.fullcycle.admin.catalog.domain.genre.GenreGateway;
import com.fullcycle.admin.catalog.domain.genre.GenreID;
import com.fullcycle.admin.catalog.domain.validation.Error;
import com.fullcycle.admin.catalog.domain.validation.ValidationHandler;
import com.fullcycle.admin.catalog.domain.validation.handler.Notification;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DefaultUpdateGenreUseCase extends UpdateGenreUseCase {

    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;


    @Override
    public UpdateGenreOutput execute(UpdateGenreCommand command) {
        final var id = command.id();
        final var categories = toCategoryID(command.categories());

        final var genre = genreGateway.findById(command.id()).orElseThrow(notFound(id));

        final var notification = Notification.create();
        notification.append(validateCategories(categories));
        notification.validate(() -> genre.update(command.name(),  command.isActive(), categories));

        if (notification.hasError()) {
            throw new NotificationException(notification);
        }

        genreGateway.update(genre);
        return UpdateGenreOutput.from(genre);
    }

    private ValidationHandler validateCategories(final List<CategoryID> ids) {
        final var notification = Notification.create();

        if (ids == null || ids.isEmpty()) {
            return notification;
        }

        final var retrievedIds = categoryGateway.existsByIds(ids);

        if (ids.size() != retrievedIds.size()) {
            final var commandsIds = new ArrayList<>(ids);
            commandsIds.removeAll(retrievedIds);

            final var missingIds = commandsIds.stream()
              .map(CategoryID::getValue)
              .map(UUID::toString)
              .collect(Collectors.joining(", "));

            notification.append(new Error("Some categories could not be found: %s".formatted(missingIds)));
        }

        return notification;
    }

    private Supplier<DomainException> notFound(final GenreID id) {
        return () -> NotFoundException.with(Genre.class, id);
    }

    private List<CategoryID> toCategoryID(List<String> categories) {
        return categories.stream()
          .map(CategoryID::from)
          .toList();
    }
}

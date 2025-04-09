package com.fullcycle.admin.catalog.domain.genre;

import com.fullcycle.admin.catalog.domain.AggregateRoot;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.errors.NotificationException;
import com.fullcycle.admin.catalog.domain.utils.InstantUtils;
import com.fullcycle.admin.catalog.domain.validation.ValidationHandler;
import com.fullcycle.admin.catalog.domain.validation.handler.Notification;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class Genre extends AggregateRoot<GenreID> {

    private String name;
    private boolean active;
    private List<CategoryID> categories;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Genre(
      final GenreID id,
      final String name,
      final boolean active,
      final List<CategoryID> categories,
      final Instant createdAt,
      final Instant updatedAt,
      final Instant deletedAt
    ) {
        super(id);
        this.name = name;
        this.active = active;
        this.categories = categories;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        sefValidate();
    }

    public static Genre newGenre(final String name, final boolean active) {
        return new Genre(
          GenreID.unique(),
          name,
          active,
          new ArrayList<>(),
          InstantUtils.now(),
          null,
          active ? null : InstantUtils.now()
        );
    }

    public Genre with(
      final GenreID id,
      final String name,
      final boolean active,
      final List<CategoryID> categories,
      final Instant createdAt,
      final Instant updatedAt,
      final Instant deletedAt
    ) {
        return new Genre(id, name, active, categories, createdAt, updatedAt, deletedAt);
    }

    public Genre with(final Genre genre) {
        return new Genre(
          genre.getId(),
          genre.getName(),
          genre.isActive(),
          new ArrayList<>(genre.categories),
          genre.getCreatedAt(),
          genre.getUpdatedAt(),
          genre.getDeletedAt()
        );
    }

    public Genre update(final String name, final boolean active, final List<CategoryID> categories) {
        this.name = name;
        this.active = active;
        this.categories = new ArrayList<>(categories != null ? categories : Collections.emptyList());
        this.updatedAt = InstantUtils.now();

        if (active) {
            activate();
        } else {
            deactivate();
        }

        sefValidate();

        return this;
    }

    @Override
    public void validate(ValidationHandler handler) {
        new GenreValidator(this, handler).validate();
    }

    public List<CategoryID> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    public void activate() {
        active = true;
        updatedAt = InstantUtils.now();
        deletedAt = null;
    }

    public void deactivate() {
        active = false;
        updatedAt = InstantUtils.now();
        deletedAt = InstantUtils.now();
    }

    public void sefValidate() {
        final var notification = Notification.create();
        validate(notification);

        if (notification.hasError()) {
            throw new NotificationException(notification);
        }
    }

    public void addCategory(final CategoryID categoryID) {
        if (categoryID != null) {
            categories.add(categoryID);
            updatedAt = InstantUtils.now();
        }
    }

    public void addCategories(final List<CategoryID> categoriesIds) {
        if (categoriesIds != null && !categoriesIds.isEmpty()) {
            categories.addAll(categoriesIds);
            updatedAt = InstantUtils.now();
        }
    }

    public void removeCategory(final CategoryID categoryID) {
        if (categoryID != null) {
            categories.remove(categoryID);
            updatedAt = InstantUtils.now();
        }
    }
}

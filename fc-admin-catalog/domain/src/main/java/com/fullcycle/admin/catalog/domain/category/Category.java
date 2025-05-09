package com.fullcycle.admin.catalog.domain.category;

import com.fullcycle.admin.catalog.domain.AggregateRoot;
import com.fullcycle.admin.catalog.domain.validation.ValidationHandler;

import lombok.Getter;
import lombok.Setter;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class Category extends AggregateRoot<CategoryID> implements Cloneable {

    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(
      final CategoryID id,
      final String name,
      final String description,
      final boolean active,
      final Instant createdAt,
      final Instant updatedAt,
      final Instant deletedAt
    ) {
        super(id);
        this.name = name;
        this.description = description;
        this.active = active;
        this.createdAt = Objects.requireNonNull(createdAt, "CreatedAt cannot be null");
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static Category newCategory(final String name, final String description, final boolean active) {
        final var id = CategoryID.unique();
        final var now = Instant.now();
        final var deletedAt = active ? null : Instant.now();
        return new Category(id, name, description, active, now, null, deletedAt);
    }

    public static Category with(
      final UUID id,
      final String name,
      final String description,
      final boolean active,
      final Instant createdAt,
      final Instant updatedAt,
      final Instant deletedAt
    ) {
        return new Category(CategoryID.from(id), name, description, active, createdAt, updatedAt, deletedAt);
    }

    @Override
    public void validate(ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }

    public Category deactivate() {
        if (getDeletedAt() == null) {
            deletedAt = Instant.now();
        }

        active = false;
        updatedAt = Instant.now();
        return this;
    }

    public Category activate() {
        deletedAt = null;
        active = true;
        updatedAt = Instant.now();
        return this;
    }

    public Category update(final String name, final String description, final boolean isActive) {
        if (isActive) {
            activate();
        } else {
            deactivate();
        }

        this.name = name;
        this.description = description;
        this.updatedAt = Instant.now();

        return this;
    }

    @Override
    public Category clone() {
        try {
            return (Category) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}


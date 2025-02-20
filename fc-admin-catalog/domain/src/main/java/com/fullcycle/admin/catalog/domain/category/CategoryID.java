package com.fullcycle.admin.catalog.domain.category;

import com.fullcycle.admin.catalog.domain.Identifier;

import lombok.Getter;
import java.util.Objects;
import java.util.UUID;

@Getter
public class CategoryID extends Identifier {

    private final UUID value;

    private CategoryID(final UUID value) {
        Objects.requireNonNull(value, "Identifier value cannot be null");
        this.value = value;
    }

    public static CategoryID unique() {
        return new CategoryID(UUID.randomUUID());
    }

    public static CategoryID from(final UUID id) {
        return new CategoryID(id);
    }

    public static CategoryID from(final String id) {
        return new CategoryID(UUID.fromString(id));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CategoryID that = (CategoryID) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}

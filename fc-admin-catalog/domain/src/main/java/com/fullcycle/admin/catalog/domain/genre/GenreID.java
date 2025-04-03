package com.fullcycle.admin.catalog.domain.genre;

import com.fullcycle.admin.catalog.domain.Identifier;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public class GenreID extends Identifier {

    private final UUID value;

    private GenreID(final UUID value) {
        Objects.requireNonNull(value, "Identifier value cannot be null");
        this.value = value;
    }

    public static GenreID unique() {
        return new GenreID(UUID.randomUUID());
    }

    public static GenreID from(final UUID id) {
        return new GenreID(id);
    }

    public static GenreID from(final String id) {
        return new GenreID(UUID.fromString(id));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GenreID that = (GenreID) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}

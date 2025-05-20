package com.fullcycle.admin.catalog.domain.video;

import com.fullcycle.admin.catalog.domain.Identifier;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public class VideoID extends Identifier {

    private final UUID value;

    private VideoID(final UUID value) {
        Objects.requireNonNull(value, "Identifier value cannot be null");
        this.value = value;
    }

    public static VideoID unique() {
        return new VideoID(UUID.randomUUID());
    }

    public static VideoID from(final UUID id) {
        return new VideoID(id);
    }

    public static VideoID from(final String id) {
        return new VideoID(UUID.fromString(id));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VideoID that = (VideoID) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

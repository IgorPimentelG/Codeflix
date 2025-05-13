package com.fullcycle.admin.catalog.domain.video;

import com.fullcycle.admin.catalog.domain.ValueObject;
import lombok.Getter;

import java.util.Objects;

public class Resource extends ValueObject {

    private final byte[] content;
    private final String contentType;
    private final String name;
    private final Type type;

    private Resource(final byte[] content, final String contentType, final String name, final Type type) {
        this.content = Objects.requireNonNull(content);
        this.contentType = Objects.requireNonNull(contentType);
        this.name = Objects.requireNonNull(name);
        this.type = Objects.requireNonNull(type);
    }

    public static Resource with(final byte[] content, final String contentType, final String name, final Type type) {
        return new Resource(content, contentType, name, type);
    }

    public enum Type {
        VIDEO,
        TRAILER,
        BANNER,
        THUMBNAIL,
        THUMBNAIL_HALF
    }

    public Type type() {
        return type;
    }

    public String name() {
        return name;
    }

    public String contentType() {
        return contentType;
    }

    public byte[] content() {
        return content;
    }
}

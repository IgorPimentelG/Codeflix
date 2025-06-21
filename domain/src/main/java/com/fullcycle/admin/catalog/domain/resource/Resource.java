package com.fullcycle.admin.catalog.domain.resource;

import com.fullcycle.admin.catalog.domain.ValueObject;

import java.util.Objects;

public class Resource extends ValueObject {

    private final String checksum;
    private final byte[] content;
    private final String contentType;
    private final String name;

    private Resource(final String checksum,final byte[] content, final String contentType, final String name) {
        this.checksum = Objects.requireNonNull(checksum);
        this.content = Objects.requireNonNull(content);
        this.contentType = Objects.requireNonNull(contentType);
        this.name = Objects.requireNonNull(name);
    }

    public static Resource with(final String checksum, final byte[] content, final String contentType, final String name) {
        return new Resource(checksum,content, contentType, name);
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

    public String checksum() {
        return checksum;
    }
}

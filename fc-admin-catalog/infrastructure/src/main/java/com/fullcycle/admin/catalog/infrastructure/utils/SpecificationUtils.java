package com.fullcycle.admin.catalog.infrastructure.utils;

import org.springframework.data.jpa.domain.Specification;

public final class SpecificationUtils {

    private SpecificationUtils() {}

    public static <T> Specification<T> like(final String prop, final String term) {
        return (root, query, cb) -> cb.like(cb.upper(root.get(prop)), like(term));
    }

    public static String like(String term) {
        return "%" + term.toUpperCase() + "%";
    }
}

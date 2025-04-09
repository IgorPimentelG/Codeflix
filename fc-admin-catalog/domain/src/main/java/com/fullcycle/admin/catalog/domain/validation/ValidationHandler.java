package com.fullcycle.admin.catalog.domain.validation;

import java.util.List;

public interface ValidationHandler {
    List<Error> getErrors();
    ValidationHandler append(Error error);
    ValidationHandler append(ValidationHandler handler);
    <T> T validate(Validation<T> validation);

    default boolean hasError() {
        return getErrors() != null && !getErrors().isEmpty();
    }

    default Error firstError() {
        if (getErrors() != null && !getErrors().isEmpty()) {
            return getErrors().get(0);
        }
        return null;
    }

   interface Validation<T> {
        T validate();
    }
}

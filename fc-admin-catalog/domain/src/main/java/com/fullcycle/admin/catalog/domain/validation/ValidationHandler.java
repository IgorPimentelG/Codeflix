package com.fullcycle.admin.catalog.domain.validation;

import java.util.List;

public interface ValidationHandler {
    List<Error> getErrors();
    ValidationHandler append(Error error);
    ValidationHandler append(ValidationHandler handler);
    ValidationHandler validate(Validation validation);

    default boolean hasError() {
        return getErrors() != null && !getErrors().isEmpty();
    }

   interface Validation {
        void validate();
    }
}

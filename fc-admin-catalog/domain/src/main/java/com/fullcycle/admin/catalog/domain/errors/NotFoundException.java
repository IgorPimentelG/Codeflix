package com.fullcycle.admin.catalog.domain.errors;

import com.fullcycle.admin.catalog.domain.AggregateRoot;
import com.fullcycle.admin.catalog.domain.Identifier;
import com.fullcycle.admin.catalog.domain.validation.Error;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class NotFoundException extends DomainException {

    private NotFoundException(final String message, final List<Error> errors) {
        super(message, errors);
    }

    public static NotFoundException with(final Class<? extends AggregateRoot<?>> aggregate, final Identifier id) {
        final var error = "%s with ID %s was not found".formatted(aggregate.getSimpleName(), id.getValue());
        return new NotFoundException(error, Collections.emptyList());
    }
}

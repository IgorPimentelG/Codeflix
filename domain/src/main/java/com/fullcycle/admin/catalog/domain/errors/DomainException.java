package com.fullcycle.admin.catalog.domain.errors;

import com.fullcycle.admin.catalog.domain.validation.Error;

import lombok.Getter;
import java.util.List;

@Getter
public class DomainException extends NoStackTraceException {

    protected final List<Error> errors;

    DomainException(final String message, final List<Error> errors) {
        super(message);
        this.errors = errors;
    }

    public static DomainException with(final Error error) {
        return new DomainException(error.message(), List.of(error));
    }

    public static DomainException with(final List<Error> errors) {
        return new DomainException("", errors);
    }
}

package com.fullcycle.admin.catalog.domain.errors;

import com.fullcycle.admin.catalog.domain.validation.Error;
import lombok.Getter;

import java.util.List;

@Getter
public class InternalException extends NoStackTraceException {

    InternalException(final String message, final Throwable t) {
        super(message, t);
    }

    public static InternalException with(final String message, final Throwable t) {
        return new InternalException(message, t);
    }
}

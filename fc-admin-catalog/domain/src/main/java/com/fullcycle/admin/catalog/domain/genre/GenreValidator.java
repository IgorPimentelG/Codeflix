package com.fullcycle.admin.catalog.domain.genre;

import com.fullcycle.admin.catalog.domain.validation.Error;
import com.fullcycle.admin.catalog.domain.validation.ValidationHandler;
import com.fullcycle.admin.catalog.domain.validation.Validator;

public class GenreValidator extends Validator {

    public static final int NAME_MAX_LENGTH = 255;
    public static final int NAME_MIN_LENGTH = 1;

    private final Genre genre;

    protected GenreValidator(final Genre genre, final ValidationHandler handler) {
        super(handler);
        this.genre = genre;
    }

    @Override
    public void validate() {
        checkNameConstrains();
    }

    private void checkNameConstrains() {
        final var name = genre.getName();

        if (name == null) {
            validationHandler().append(new Error("Name cannot be null"));
            return;
        }

        if (name.isBlank()) {
            validationHandler().append(new  Error("Name cannot be empty"));
            return;
        }

        final int length = name.trim().length();
        if (length > NAME_MAX_LENGTH || length < NAME_MIN_LENGTH) {
            validationHandler().append(new Error("Name must be between 1 and 255 characters"));
        }
    }
}

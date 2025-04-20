package com.fullcycle.admin.catalog.domain.castmember;

import com.fullcycle.admin.catalog.domain.validation.Error;
import com.fullcycle.admin.catalog.domain.validation.ValidationHandler;
import com.fullcycle.admin.catalog.domain.validation.Validator;

public class CastMemberValidator extends Validator {

    private final CastMember castMember;

    public static final int NAME_MAX_LENGTH = 255;
    public static final int NAME_MIN_LENGTH = 1;

    public CastMemberValidator(final CastMember castMember, final ValidationHandler handler) {
        super(handler);
        this.castMember = castMember;
    }

    @Override
    public void validate() {
        checkNameConstrains();
        checkTypeConstraints();
    }

    private void checkNameConstrains() {
        final var name = castMember.getName();

        if (name == null) {
            validationHandler().append(new Error("Name cannot be null"));
            return;
        }

        if (name.isBlank()) {
            validationHandler().append(new Error("Name cannot be empty"));
            return;
        }

        final int length = name.trim().length();
        if (length > NAME_MAX_LENGTH || length < NAME_MIN_LENGTH) {
            validationHandler().append(new Error("Name must be between 1 and 255 characters"));
        }
    }

    private void checkTypeConstraints() {
        final var type = castMember.getType();

        if (type == null) {
            validationHandler().append(new Error("Type cannot be null"));
        }
    }
}

package com.fullcycle.admin.catalog.domain.video;

import com.fullcycle.admin.catalog.domain.validation.Error;
import com.fullcycle.admin.catalog.domain.validation.ValidationHandler;
import com.fullcycle.admin.catalog.domain.validation.Validator;

public class VideoValidator extends Validator {

    public static final int MIN_LENGTH = 1;
    public static final int TITLE_MAX_LENGTH = 255;
    public static final int DESCRIPTION_MAX_LENGTH = 1000;

    private final Video video;

    protected VideoValidator(final Video video, final ValidationHandler handler) {
        super(handler);
        this.video = video;
    }

    @Override
    public void validate() {
        checkTitleConstrains();
        checkDescriptionConstrains();
        checkLaunchedConstrains();
        checkRatingConstrains();
    }

    private void checkTitleConstrains() {
        final var title = video.getTitle();

        if (title == null) {
            validationHandler().append(new Error("Title cannot be null"));
            return;
        }

        if (title.isBlank()) {
            validationHandler().append(new Error("Title cannot be empty"));
            return;
        }

        final int length = title.trim().length();
        if (length > TITLE_MAX_LENGTH || length < MIN_LENGTH) {
            validationHandler().append(new Error("Title must be between 1 and 255 characters"));
        }
    }

    private void checkDescriptionConstrains() {
        final var description = video.getDescription();

        if (description == null) {
            validationHandler().append(new Error("Description cannot be null"));
            return;
        }

        if (description.isBlank()) {
            validationHandler().append(new Error("Description cannot be empty"));
            return;
        }

        final int length = description.trim().length();
        if (length > DESCRIPTION_MAX_LENGTH || length < MIN_LENGTH) {
            validationHandler().append(new Error("Description must be between 1 and 1000 characters"));
        }
    }

    private void checkLaunchedConstrains() {
        final var launchedAt = video.getLaunchedAt();

        if (launchedAt == null) {
            validationHandler().append(new Error("LaunchedAt cannot be null"));
        }
    }

    private void checkRatingConstrains() {
        final var rating = video.getRating();

        if (rating == null) {
            validationHandler().append(new Error("Rating cannot be null"));
        }
    }
}

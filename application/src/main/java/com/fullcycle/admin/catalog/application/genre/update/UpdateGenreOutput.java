package com.fullcycle.admin.catalog.application.genre.update;

import com.fullcycle.admin.catalog.domain.genre.Genre;

import java.util.UUID;

public record UpdateGenreOutput(UUID id) {
    public static UpdateGenreOutput from(final Genre genre) {
        return new UpdateGenreOutput(genre.getId().getValue());
    }
}

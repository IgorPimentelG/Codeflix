package com.fullcycle.admin.catalog.application.genre.create;

import com.fullcycle.admin.catalog.domain.genre.Genre;
import com.fullcycle.admin.catalog.domain.genre.GenreID;

import java.util.UUID;

public record CreateGenreOutput(UUID genreId) {
    public static CreateGenreOutput from(final Genre genre) {
        return new CreateGenreOutput(genre.getId().getValue());
    }

    public static CreateGenreOutput from(final GenreID id) {
        return new CreateGenreOutput(id.getValue());
    }
}

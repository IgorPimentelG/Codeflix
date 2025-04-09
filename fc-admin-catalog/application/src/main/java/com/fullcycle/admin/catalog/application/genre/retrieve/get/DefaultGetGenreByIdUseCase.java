package com.fullcycle.admin.catalog.application.genre.retrieve.get;

import com.fullcycle.admin.catalog.domain.errors.NotFoundException;
import com.fullcycle.admin.catalog.domain.genre.Genre;
import com.fullcycle.admin.catalog.domain.genre.GenreGateway;
import com.fullcycle.admin.catalog.domain.genre.GenreID;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class DefaultGetGenreByIdUseCase extends GetGenreByIdUseCase {

    private final GenreGateway genreGateway;

    @Override
    public GenreOutput execute(final UUID id) {
        final var genreID = GenreID.from(id);
        final var genre = genreGateway.findById(genreID)
          .orElseThrow(() -> NotFoundException.with(Genre.class, genreID));
        return GenreOutput.from(genre);
    }
}

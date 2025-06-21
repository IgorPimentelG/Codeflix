package com.fullcycle.admin.catalog.application.genre.retrieve.get;

import com.fullcycle.admin.catalog.domain.errors.NotFoundException;
import com.fullcycle.admin.catalog.domain.genre.Genre;
import com.fullcycle.admin.catalog.domain.genre.GenreGateway;
import com.fullcycle.admin.catalog.domain.genre.GenreID;
import lombok.RequiredArgsConstructor;

import java.util.UUID;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class DefaultGetGenreByIdUseCase extends GetGenreByIdUseCase {

    private final GenreGateway genreGateway;

    @Override
    public GenreOutput execute(final UUID id) {
        final var genreID = GenreID.from(id);
        return genreGateway.findById(genreID)
          .map(GenreOutput::from)
          .orElseThrow(notFound(genreID));
    }

    private Supplier<NotFoundException> notFound(GenreID genreID) {
        return () -> NotFoundException.with(Genre.class, genreID);
    }
}

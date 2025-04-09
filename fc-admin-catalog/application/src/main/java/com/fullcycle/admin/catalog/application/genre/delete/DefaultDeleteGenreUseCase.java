package com.fullcycle.admin.catalog.application.genre.delete;

import com.fullcycle.admin.catalog.domain.genre.GenreGateway;
import com.fullcycle.admin.catalog.domain.genre.GenreID;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class DefaultDeleteGenreUseCase extends DeleteGenreUseCase {

    private final GenreGateway genreGateway;

    @Override
    public void execute(final UUID id) {
        genreGateway.deleteById(GenreID.from(id));
    }
}

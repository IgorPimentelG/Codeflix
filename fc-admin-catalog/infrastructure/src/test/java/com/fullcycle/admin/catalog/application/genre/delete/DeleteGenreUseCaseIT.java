package com.fullcycle.admin.catalog.application.genre.delete;

import com.fullcycle.admin.catalog.IntegrationTest;
import com.fullcycle.admin.catalog.domain.genre.Genre;
import com.fullcycle.admin.catalog.domain.genre.GenreGateway;
import com.fullcycle.admin.catalog.domain.genre.GenreID;
import com.fullcycle.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@IntegrationTest
public class DeleteGenreUseCaseIT {

    @Autowired
    private DeleteGenreUseCase useCase;

    @Autowired
    private GenreGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void givenValidGenreId_whenCallsDeleteGenre_shouldBeOk() {
        final var genre = Genre.newGenre("any name", true);
        final var expectedId = genre.getId();

        genreGateway.create(genre);

        assertEquals(1, genreRepository.count());
        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        assertEquals(0, genreRepository.count());
    }

    @Test
    public void givenInvalidGenreId_whenCallsDeleteGenre_shouldBeOk() {
        final var expectedId = GenreID.from(UUID.randomUUID());
        assertEquals(0, genreRepository.count());
        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        assertEquals(0, genreRepository.count());
    }
}

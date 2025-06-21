package com.fullcycle.admin.catalog.application.genre.delete;

import com.fullcycle.admin.catalog.application.UseCaseTest;
import com.fullcycle.admin.catalog.domain.genre.Genre;
import com.fullcycle.admin.catalog.domain.genre.GenreGateway;
import com.fullcycle.admin.catalog.domain.genre.GenreID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DeleteGenreUseCaseTest extends UseCaseTest {

    @Mock
    private GenreGateway genreGateway;

    @InjectMocks
    private DefaultDeleteGenreUseCase useCase;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void givenValidGenreId_whenCallsDeleteGenre_shouldBeOk() {
        final var genre = Genre.newGenre("any name", true);
        final var expectedId = genre.getId();

        doNothing().when(genreGateway).deleteById(any());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(genreGateway, times(1)).deleteById(expectedId);
    }

    @Test
    public void givenInvalidGenreId_whenCallsDeleteGenre_shouldBeOk() {
        final var genre = Genre.newGenre("any name", true);
        final var expectedId = GenreID.from(UUID.randomUUID());

        doNothing().when(genreGateway).deleteById(any());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(genreGateway, times(1)).deleteById(expectedId);
    }

    @Test
    public void givenValidGenreId_whenCallsDeleteGenreAndGatewayThrowsUnexpectedError_shouldReceiveException() {
        final var genre = Genre.newGenre("any name", true);
        final var expectedId = genre.getId();

        doThrow(new IllegalStateException("Gateway error")).when(genreGateway).deleteById(any());
        assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        verify(genreGateway, times(1)).deleteById(expectedId);
    }
}
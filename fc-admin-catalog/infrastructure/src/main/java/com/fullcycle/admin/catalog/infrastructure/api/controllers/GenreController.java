package com.fullcycle.admin.catalog.infrastructure.api.controllers;

import com.fullcycle.admin.catalog.application.genre.create.CreateGenreCommand;
import com.fullcycle.admin.catalog.application.genre.create.CreateGenreUseCase;
import com.fullcycle.admin.catalog.application.genre.delete.DeleteGenreUseCase;
import com.fullcycle.admin.catalog.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.fullcycle.admin.catalog.application.genre.retrieve.list.ListGenreUseCase;
import com.fullcycle.admin.catalog.application.genre.update.UpdateGenreCommand;
import com.fullcycle.admin.catalog.application.genre.update.UpdateGenreUseCase;
import com.fullcycle.admin.catalog.domain.pagination.Pagination;
import com.fullcycle.admin.catalog.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalog.infrastructure.api.GenreAPI;
import com.fullcycle.admin.catalog.infrastructure.genre.models.CreateGenreRequest;
import com.fullcycle.admin.catalog.infrastructure.genre.models.GenreListResponse;
import com.fullcycle.admin.catalog.infrastructure.genre.models.GenreResponse;
import com.fullcycle.admin.catalog.infrastructure.genre.models.UpdateGenreRequest;
import com.fullcycle.admin.catalog.infrastructure.genre.presenters.GenreApiPresenter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class GenreController implements GenreAPI {

    private final CreateGenreUseCase createGenreUseCase;
    private final GetGenreByIdUseCase getGenreByIdUseCase;
    private final UpdateGenreUseCase updateGenreUseCase;
    private final DeleteGenreUseCase deleteGenreUseCase;
    private final ListGenreUseCase listGenreUseCase;

    @Override
    public ResponseEntity<?> create(final CreateGenreRequest input) {
        final var command = CreateGenreCommand.with(input.name(), input.active(), input.categories());
        final var output = createGenreUseCase.execute(command);
        return ResponseEntity.created(URI.create("/genres/" + output.genreId().toString())).body(output);
    }

    @Override
    public Pagination<GenreListResponse> list(
      final String search,
      final int page,
      final int perPage,
      final String sort,
      final String direction
    ) {
        final var query = new SearchQuery(page, perPage, search, sort, direction);
        return listGenreUseCase.execute(query).map(GenreApiPresenter::present);
    }

    @Override
    public GenreResponse getById(final UUID id) {
        final var genre = getGenreByIdUseCase.execute(id);
        return GenreApiPresenter.present(genre);
    }

    @Override
    public ResponseEntity<?> update(final UUID id, final UpdateGenreRequest input) {
        final var command = UpdateGenreCommand.with(id, input.name(), input.active(), input.categories());
        final var output = updateGenreUseCase.execute(command);
        return ResponseEntity.created(URI.create("/genres/" + output.id().toString())).body(output);
    }

    @Override
    public void delete(final UUID id) {
        deleteGenreUseCase.execute(id);
        ResponseEntity.noContent().build();
    }
}

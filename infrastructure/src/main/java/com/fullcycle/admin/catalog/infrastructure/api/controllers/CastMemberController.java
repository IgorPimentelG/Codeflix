package com.fullcycle.admin.catalog.infrastructure.api.controllers;

import com.fullcycle.admin.catalog.application.castmember.create.CreateCastMemberCommand;
import com.fullcycle.admin.catalog.application.castmember.create.CreateCastMemberUseCase;
import com.fullcycle.admin.catalog.application.castmember.delete.DeleteCastMemberUseCase;
import com.fullcycle.admin.catalog.application.castmember.retrieve.get.GetCastMemberUseCase;
import com.fullcycle.admin.catalog.application.castmember.retrieve.list.ListCastMemberUseCase;
import com.fullcycle.admin.catalog.application.castmember.update.UpdateCastMemberCommand;
import com.fullcycle.admin.catalog.application.castmember.update.UpdateCastMemberUseCase;
import com.fullcycle.admin.catalog.domain.pagination.Pagination;
import com.fullcycle.admin.catalog.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalog.infrastructure.api.CastMemberAPI;
import com.fullcycle.admin.catalog.infrastructure.castmember.models.CastMemberListResponse;
import com.fullcycle.admin.catalog.infrastructure.castmember.models.CastMemberResponse;
import com.fullcycle.admin.catalog.infrastructure.castmember.models.CreateCastMemberRequest;
import com.fullcycle.admin.catalog.infrastructure.castmember.models.UpdateCastMemberRequest;
import com.fullcycle.admin.catalog.infrastructure.castmember.presenters.CastMemberApiPresenter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CastMemberController implements CastMemberAPI {

    private final CreateCastMemberUseCase createCastMemberUseCase;
    private final GetCastMemberUseCase getCastMemberByIdUseCase;
    private final ListCastMemberUseCase listCastMemberUseCase;
    private final DeleteCastMemberUseCase deleteCastMemberByIdUseCase;
    private final UpdateCastMemberUseCase updateCastMemberUseCase;

    @Override
    public ResponseEntity<?> createCastMember(CreateCastMemberRequest input) {
        final var command = CreateCastMemberCommand.with(input.name(), input.type());
        final var output = createCastMemberUseCase.execute(command);
        return ResponseEntity.created(URI.create("/cast-members/" + output.id().toString())).body(output);
    }

    @Override
    public Pagination<CastMemberListResponse> listCastMembers(String search, int page, int perPage, String sort, String direction) {
        final var query = new SearchQuery(page, perPage, search, sort, direction);
        return listCastMemberUseCase.execute(query).map(CastMemberApiPresenter::present);
    }

    @Override
    public CastMemberResponse getCastMemberById(UUID id) {
        final var output = getCastMemberByIdUseCase.execute(id);
        return CastMemberApiPresenter.present(output);
    }

    @Override
    public ResponseEntity<?> updateCastMember(UUID id, UpdateCastMemberRequest input) {
        final var command = UpdateCastMemberCommand.with(id, input.name(), input.type());
        final var output = updateCastMemberUseCase.execute(command);
        return ResponseEntity.ok(output);
    }

    @Override
    public void deleteCastMember(UUID id) {
        deleteCastMemberByIdUseCase.execute(id);
    }
}

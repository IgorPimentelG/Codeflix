package com.fullcycle.admin.catalog.application.castmember.retrieve.list;

import com.fullcycle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalog.domain.pagination.Pagination;
import com.fullcycle.admin.catalog.domain.pagination.SearchQuery;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class DefaultListCastMemberUseCase extends ListCastMemberUseCase{

    private final CastMemberGateway castMemberGateway;

    @Override
    public Pagination<CastMemberListOutput> execute(final SearchQuery query) {
        return castMemberGateway.findAll(query).map(CastMemberListOutput::from);
    }
}

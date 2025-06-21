package com.fullcycle.admin.catalog.domain.castmember;

import com.fullcycle.admin.catalog.domain.pagination.Pagination;
import com.fullcycle.admin.catalog.domain.pagination.SearchQuery;

import java.util.List;
import java.util.Optional;

public interface CastMemberGateway {

    CastMember create(CastMember castMember);
    CastMember update(CastMember castMember);
    void deleteById(CastMemberID id);
    Optional<CastMember> findById(CastMemberID id);
    Pagination<CastMember> findAll(SearchQuery query);
    List<CastMemberID> existsByIds(Iterable<CastMemberID> id);
}

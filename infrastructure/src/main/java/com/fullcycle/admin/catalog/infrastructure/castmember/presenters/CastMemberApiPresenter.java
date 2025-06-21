package com.fullcycle.admin.catalog.infrastructure.castmember.presenters;

import com.fullcycle.admin.catalog.application.castmember.retrieve.get.CastMemberOutput;
import com.fullcycle.admin.catalog.application.castmember.retrieve.list.CastMemberListOutput;
import com.fullcycle.admin.catalog.application.category.retrieve.get.CategoryOutput;
import com.fullcycle.admin.catalog.application.category.retrieve.list.CategoryListOutput;
import com.fullcycle.admin.catalog.infrastructure.castmember.models.CastMemberListResponse;
import com.fullcycle.admin.catalog.infrastructure.castmember.models.CastMemberResponse;
import com.fullcycle.admin.catalog.infrastructure.category.models.CategoryListResponse;
import com.fullcycle.admin.catalog.infrastructure.category.models.CategoryResponse;

public interface CastMemberApiPresenter {

    static CastMemberResponse present(final CastMemberOutput output) {
        return new CastMemberResponse(
          output.id().toString(),
          output.name(),
          output.type(),
          output.createdAt(),
          output.updatedAt()
        );
    }

    static CastMemberListResponse present(final CastMemberListOutput output) {
        return new CastMemberListResponse(
          output.id().toString(),
          output.name(),
          output.type(),
          output.createdAt()
        );
    }
}

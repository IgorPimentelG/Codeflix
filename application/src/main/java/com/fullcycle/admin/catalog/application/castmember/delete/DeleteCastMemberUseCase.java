package com.fullcycle.admin.catalog.application.castmember.delete;

import com.fullcycle.admin.catalog.application.UnitUseCase;

import java.util.UUID;

public sealed abstract class DeleteCastMemberUseCase
    extends UnitUseCase<UUID>
    permits DefaultDeleteCastMemberUseCase
{}

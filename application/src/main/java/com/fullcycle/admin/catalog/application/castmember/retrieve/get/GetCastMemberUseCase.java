package com.fullcycle.admin.catalog.application.castmember.retrieve.get;

import com.fullcycle.admin.catalog.application.UseCase;

import java.util.UUID;

public sealed abstract class GetCastMemberUseCase
    extends UseCase<UUID, CastMemberOutput>
    permits DefaultGetCastMemberUseCase
{}

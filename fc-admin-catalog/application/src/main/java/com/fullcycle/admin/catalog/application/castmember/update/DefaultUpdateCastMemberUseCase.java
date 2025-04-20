package com.fullcycle.admin.catalog.application.castmember.update;

import com.fullcycle.admin.catalog.domain.castmember.CastMember;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalog.domain.errors.NotFoundException;
import com.fullcycle.admin.catalog.domain.errors.NotificationException;
import com.fullcycle.admin.catalog.domain.validation.handler.Notification;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@RequiredArgsConstructor
public final class DefaultUpdateCastMemberUseCase extends UpdateCastMemberUseCase {

    private final CastMemberGateway castMemberGateway;

    @Override
    public UpdateCastMemberOutput execute(final UpdateCastMemberCommand command) {
        final var id = CastMemberID.from(command.id());

        final var castMember = this.castMemberGateway.findById(id).orElseThrow(notFound(id));

        final var notification = Notification.create();
        notification.validate(() -> castMember.update(command.name(), command.type()));

        if (notification.hasError()) {
            throw new NotificationException(notification);
        }

        final var output = castMemberGateway.update(castMember);
        return UpdateCastMemberOutput.from(output);
    }

    private Supplier<NotFoundException> notFound(CastMemberID id) {
        return () -> NotFoundException.with(CastMember.class, id);
    }
}

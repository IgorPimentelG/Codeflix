package com.fullcycle.admin.catalog.application.castmember.create;

import com.fullcycle.admin.catalog.domain.castmember.CastMember;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalog.domain.errors.NotificationException;
import com.fullcycle.admin.catalog.domain.validation.handler.Notification;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class DefaultCreateCastMemberUseCase extends CreateCastMemberUseCase {

    private final CastMemberGateway castMemberGateway;

    @Override
    public CreateCastMemberOutput execute(final CreateCastMemberCommand command) {
        final var notification = Notification.create();
        final var castMember = notification.validate(() -> CastMember.newMember(command.name(), command.type()));

        if (notification.hasError()) {
            throw new NotificationException(notification);
        }

        final var output = castMemberGateway.create(castMember);
        return CreateCastMemberOutput.from(output);
    }
}

package com.fullcycle.admin.catalog.domain.castmember;

import com.fullcycle.admin.catalog.domain.AggregateRoot;
import com.fullcycle.admin.catalog.domain.errors.NotificationException;
import com.fullcycle.admin.catalog.domain.validation.ValidationHandler;
import com.fullcycle.admin.catalog.domain.validation.handler.Notification;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class CastMember extends AggregateRoot<CastMemberID> {

    private String name;
    private CastMemberType type;
    private Instant createdAt;
    private Instant updatedAt;

    protected CastMember(
      final CastMemberID id,
      final String name,
      final CastMemberType type,
      final Instant createdAt,
      final Instant updatedAt
    ) {
        super(id);
        this.name = name;
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

        selfValidate();
    }

    public static CastMember newMember(final String name, final CastMemberType type) {
        final var id = CastMemberID.unique();
        return new CastMember(id, name, type, Instant.now(), null);
    }

    public CastMember update(final String name, final CastMemberType type) {
        this.name = name;
        this.type = type;
        this.updatedAt = Instant.now();
        selfValidate();
        return this;
    }

    public static CastMember with(
      final CastMemberID id,
      final String name,
      final CastMemberType type,
      final Instant createdAt,
      final Instant updatedAt
    ) {
        return new CastMember(id, name, type, createdAt, updatedAt);
    }

    public static CastMember with(CastMember castMember) {
        return new CastMember(
          castMember.getId(),
          castMember.getName(),
          castMember.getType(),
          castMember.getCreatedAt(),
          castMember.getUpdatedAt()
        );
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);

        if (notification.hasError()) {
            throw new NotificationException(notification);
        }
    }

    @Override
    public void validate(ValidationHandler handler) {
        new CastMemberValidator(this, handler).validate();
    }
}
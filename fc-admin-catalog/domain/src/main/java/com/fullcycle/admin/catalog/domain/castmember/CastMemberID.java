package com.fullcycle.admin.catalog.domain.castmember;

import com.fullcycle.admin.catalog.domain.Identifier;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public class CastMemberID extends Identifier {

    private final UUID value;

    private CastMemberID(final UUID value) {
        Objects.requireNonNull(value, "Identifier value cannot be null");
        this.value = value;
    }

    public static CastMemberID unique() {
        return new CastMemberID(UUID.randomUUID());
    }

    public static CastMemberID from(final UUID id) {
        return new CastMemberID(id);
    }

    public static CastMemberID from(final String id) {
        return new CastMemberID(UUID.fromString(id));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CastMemberID that = (CastMemberID) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

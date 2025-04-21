package com.fullcycle.admin.catalog.infrastructure.castmember.persistence;

import com.fullcycle.admin.catalog.domain.castmember.CastMember;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "CastMember")
@Table(name = "cast_members")
public class CastMemberJpaEntity {

    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CastMemberType type;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @Column(name = "updated_at", columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    public static CastMemberJpaEntity from(final CastMember castMember) {
        return new CastMemberJpaEntity(
            castMember.getId().getValue(),
            castMember.getName(),
            castMember.getType(),
            castMember.getCreatedAt(),
            castMember.getUpdatedAt()
        );
    }

    public CastMember toAggregate() {
        return CastMember.with(
          CastMemberID.from(id),
          name,
          type,
          createdAt,
          updatedAt
        );
    }
}

package com.fullcycle.admin.catalog.infrastructure.category.persistence;

import com.fullcycle.admin.catalog.domain.category.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class CategoryJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(length = 4000, nullable = true)
    private String description;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @Column(name = "updated_at", columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    @Column(name = "deleted_at", columnDefinition = "DATETIME(6)")
    private Instant deletedAt;

    public static CategoryJpaEntity from(final Category category) {
        return new CategoryJpaEntity(
                category.getId().getValue(),
                category.getName(),
                category.getDescription(),
                category.isActive(),
                category.getCreatedAt(),
                category.getUpdatedAt(),
                category.getDeletedAt()
        );
    }

    public Category toAggregate() {
        return Category.with(
           getId(),
           getName(),
           getDescription(),
           isActive(),
           getCreatedAt(),
           getUpdatedAt(),
           getDeletedAt()
        );
    }
}

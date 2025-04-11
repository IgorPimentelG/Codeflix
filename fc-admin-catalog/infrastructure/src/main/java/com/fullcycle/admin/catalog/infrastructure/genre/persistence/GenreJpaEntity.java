package com.fullcycle.admin.catalog.infrastructure.genre.persistence;

import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.genre.Genre;
import com.fullcycle.admin.catalog.domain.genre.GenreID;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.EAGER;

@Entity
@Table(name = "genres")
@Data
@NoArgsConstructor
public class GenreJpaEntity {

    @Id
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private boolean active;

    @OneToMany(mappedBy = "genre", cascade = ALL, fetch = EAGER, orphanRemoval = true)
    private Set<GenreCategoryJpaEntity> categories;

    @Column(name = "created_at", nullable = false,  columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @Column(name = "updated_at", columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    @Column(name = "deleted_at",  columnDefinition = "DATETIME(6)")
    private Instant deletedAt;

    private GenreJpaEntity(
      final UUID id,
      final String name,
      final boolean isActive,
      final Instant createdAt,
      final Instant updatedAt,
      final Instant deletedAt
    ) {
        this.id = id;
        this.name = name;
        this.active = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.categories = new HashSet<>();
    }

    public static GenreJpaEntity from(final Genre genre) {
        final var entity = new GenreJpaEntity(
          genre.getId().getValue(),
          genre.getName(),
          genre.isActive(),
          genre.getCreatedAt(),
          genre.getUpdatedAt(),
          genre.getDeletedAt()
        );

        genre.getCategories().forEach(entity::addCategory);

        return entity;
    }

    public Genre toAggregate() {
        return Genre.with(
          GenreID.from(getId()),
          getName(),
          isActive(),
          getCategories().stream().map(category -> CategoryID.from(category.getId().getCategoryId())).toList(),
          getCreatedAt(),
          getUpdatedAt(),
          getDeletedAt()
        );
    }

    private void addCategory(final CategoryID id) {
        this.categories.add(GenreCategoryJpaEntity.from(this, id));
    }

    private void removeCategory(final CategoryID id) {
        this.categories.remove(GenreCategoryJpaEntity.from(this, id));
    }
}

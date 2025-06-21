package com.fullcycle.admin.catalog.infrastructure.genre.persistence;

import com.fullcycle.admin.catalog.domain.category.CategoryID;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "genres_categories")
@Data
@NoArgsConstructor
public class GenreCategoryJpaEntity {

    @EmbeddedId
    private GenreCategoryID id;

    @ManyToOne
    @MapsId("genreId")
    private GenreJpaEntity genre;

    private GenreCategoryJpaEntity(final GenreJpaEntity genre, final CategoryID categoryID) {
        this.id = GenreCategoryID.from(genre.getId(), categoryID.getValue());
        this.genre = genre;
    }

    public static GenreCategoryJpaEntity from(final GenreJpaEntity genre, final CategoryID categoryID) {
        return new GenreCategoryJpaEntity(genre, categoryID);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GenreCategoryJpaEntity that = (GenreCategoryJpaEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

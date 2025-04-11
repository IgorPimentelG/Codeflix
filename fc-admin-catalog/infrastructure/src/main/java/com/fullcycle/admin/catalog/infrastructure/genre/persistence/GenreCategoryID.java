package com.fullcycle.admin.catalog.infrastructure.genre.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GenreCategoryID implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "genre_id", nullable = false)
    private UUID genreId;

    @Column(name = "category_id", nullable = false)
    private UUID categoryId;

    public static GenreCategoryID from(final UUID genreId, final UUID categoryId) {
        return new GenreCategoryID(genreId, categoryId);
    }
}

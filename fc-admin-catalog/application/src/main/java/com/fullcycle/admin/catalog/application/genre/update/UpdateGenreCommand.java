package com.fullcycle.admin.catalog.application.genre.update;

import com.fullcycle.admin.catalog.domain.genre.GenreID;

import java.util.List;
import java.util.UUID;

public record UpdateGenreCommand(GenreID id, String name, boolean isActive, List<String> categories) {

    public static UpdateGenreCommand with(
      final UUID id,
      final String name,
      final boolean isActive,
      final List<String> categories
    ) {
        return new UpdateGenreCommand(GenreID.from(id), name, isActive, categories);
    }
}

package com.fullcycle.admin.catalog.infrastructure.genre.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface GenreRepository extends JpaRepository<GenreJpaEntity, UUID> {

    Page<GenreJpaEntity> findAll(Specification<GenreJpaEntity> filters, Pageable page);

    @Query("SELECT g.id FROM Genre g where g.id IN :ids")
    List<String> existsByIds(@Param("ids") List<UUID> ids);
}

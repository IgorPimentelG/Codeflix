package com.fullcycle.admin.catalog.infrastructure.genre.persistence;

import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GenreRepository extends JpaRepository<GenreJpaEntity, UUID> {

    Page<GenreJpaEntity> findAll(Specification<GenreJpaEntity> filters, Pageable page);
}

package com.fullcycle.admin.catalog.infrastructure.category.persistence;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<CategoryJpaEntity, UUID> {

    Page<CategoryJpaEntity> findAll(Specification<CategoryJpaEntity> filters, Pageable page);

    @Query("SELECT c.id FROM Category c where c.id IN :ids")
    List<String> existsByIds(@Param("ids") List<UUID> ids);
}

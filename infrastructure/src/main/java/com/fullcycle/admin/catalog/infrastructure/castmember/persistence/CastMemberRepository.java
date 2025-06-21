package com.fullcycle.admin.catalog.infrastructure.castmember.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CastMemberRepository extends JpaRepository<CastMemberJpaEntity, UUID> {

    Page<CastMemberJpaEntity> findAll(Specification<CastMemberJpaEntity> specification, Pageable page);

    @Query("SELECT cm.id FROM CastMember cm where cm.id IN :ids")
    List<String> existsByIds(@Param("ids") List<UUID> ids);
}
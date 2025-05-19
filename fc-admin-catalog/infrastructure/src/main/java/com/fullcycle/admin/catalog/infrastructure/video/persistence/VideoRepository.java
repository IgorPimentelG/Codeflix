package com.fullcycle.admin.catalog.infrastructure.video.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;
import java.util.UUID;

public interface VideoRepository extends JpaRepository<VideoJpaEntity, UUID> {

	@Query("""
		SELECT new com.fullcycle.admin.catalog.infrastructure.video.persistence.VideoPreview(
			v.id as id,
			v.title as title,
			v.description as description,
			v.createdAt as createdAt,
			v.updatedAt as updatedAt
		)
		FROM Video v
			JOIN v.castMembers cm
			JOIN v.categories c
			JOIN v.genres g
		WHERE
			( :terms IS NULL OR UPPER(v.title) LIKE :terms )
		AND
			( :castMembers IS NULL OR castMembers.id.castMemberId in :castMembers )
		AND
			( :genres IS NULL OR genres.id.genreId in :genres )
		AND
			( :categories IS NULL OR categories.id.categoryId in :categories )
	""")
	Page<VideoPreview> findAll(
	  @Param("terms") String terms,
	  @Param("categories") Set<String> categories,
	  @Param("genres") Set<String> genres,
	  @Param("castMembers") Set<String> castMembers,
	  Pageable page
	);
}

package com.fullcycle.admin.catalog.infrastructure.video.persistence;

import com.fullcycle.admin.catalog.domain.video.VideoPreview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;
import java.util.UUID;

public interface VideoRepository extends JpaRepository<VideoJpaEntity, UUID> {

	@Query("""
		SELECT distinct new com.fullcycle.admin.catalog.domain.video.VideoPreview(
			v.id as id,
			v.title as title,
			v.description as description,
			v.createdAt as createdAt,
			v.updatedAt as updatedAt
		)
		FROM Video v
			LEFT JOIN v.castMembers members
			LEFT JOIN v.categories categories
			LEFT JOIN v.genres genres
		WHERE
			( :#{#terms} IS NULL OR UPPER(v.title) LIKE :#{#terms} )
		AND
			( :#{#castMembers} IS NULL OR members.id.castMemberID in :#{#castMembers} )
		AND
			( :#{#genres} IS NULL OR genres.id.genreID in :#{#genres} )
		AND
			( :#{#categories} IS NULL OR categories.id.categoryID in :#{#categories} )
	""")
	Page<VideoPreview> findAll(
	  @Param("terms") String terms,
	  @Param("categories") Set<String> categories,
	  @Param("genres") Set<String> genres,
	  @Param("castMembers") Set<String> castMembers,
	  Pageable page
	);
}

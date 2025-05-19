package com.fullcycle.admin.catalog.infrastructure.video.persistence;

import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.genre.GenreID;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "videos_genres")
@Entity(name = "VideoGenre")
public class VideoGenreJpaEntity {

	@EmbeddedId
	private VideoGenreID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("videoId")
	private VideoJpaEntity video;

	public static VideoGenreJpaEntity from(final VideoJpaEntity video, GenreID genreID) {
		return new VideoGenreJpaEntity(
		  VideoGenreID.from(video.getId(), genreID.getValue()),
		  video
		);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		VideoGenreJpaEntity that = (VideoGenreJpaEntity) o;
		return Objects.equals(id, that.id) && Objects.equals(video, that.video);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, video);
	}
}

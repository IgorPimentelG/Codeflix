package com.fullcycle.admin.catalog.infrastructure.video.persistence;

import com.fullcycle.admin.catalog.domain.category.CategoryID;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "videos_categories")
@Entity(name = "VideoCategory")
public class VideoCategoryJpaEntity {

	@EmbeddedId
	private VideoCategoryID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("videoId")
	private VideoJpaEntity video;

	public static VideoCategoryJpaEntity from(final VideoJpaEntity video, CategoryID categoryID) {
		return new VideoCategoryJpaEntity(
		  VideoCategoryID.from(video.getId(), categoryID.getValue()),
		  video
		);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		VideoCategoryJpaEntity that = (VideoCategoryJpaEntity) o;
		return Objects.equals(id, that.id) && Objects.equals(video, that.video);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, video);
	}
}

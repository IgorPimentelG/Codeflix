package com.fullcycle.admin.catalog.infrastructure.video.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class VideoGenreID implements Serializable {

	@Column(name = "video_id", nullable = false)
	private UUID videoID;

	@Column(name = "genre_id", nullable = false)
	private UUID genreID;

	public static VideoGenreID from(final UUID videoID, final UUID genreID) {
		return new VideoGenreID(videoID, genreID);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		VideoGenreID that = (VideoGenreID) o;
		return Objects.equals(videoID, that.videoID) && Objects.equals(genreID, that.genreID);
	}

	@Override
	public int hashCode() {
		return Objects.hash(videoID, genreID);
	}
}

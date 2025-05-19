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
public class VideoCategoryID implements Serializable {

	@Column(name = "video_id", nullable = false)
	private UUID videoID;

	@Column(name = "category_id", nullable = false)
	private UUID categoryID;

	public static VideoCategoryID from(final UUID videoID, final UUID categoryID) {
		return new VideoCategoryID(videoID, categoryID);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		VideoCategoryID that = (VideoCategoryID) o;
		return Objects.equals(videoID, that.videoID) && Objects.equals(categoryID, that.categoryID);
	}

	@Override
	public int hashCode() {
		return Objects.hash(videoID, categoryID);
	}
}

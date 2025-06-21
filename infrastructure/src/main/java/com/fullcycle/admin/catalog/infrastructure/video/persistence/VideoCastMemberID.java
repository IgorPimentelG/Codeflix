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
public class VideoCastMemberID implements Serializable {

	@Column(name = "video_id", nullable = false)
	private UUID videoID;

	@Column(name = "cast_member_id", nullable = false)
	private UUID castMemberID;

	public static VideoCastMemberID from(final UUID videoID, final UUID castMemberID) {
		return new VideoCastMemberID(videoID, castMemberID);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		VideoCastMemberID that = (VideoCastMemberID) o;
		return Objects.equals(videoID, that.videoID) && Objects.equals(castMemberID, that.castMemberID);
	}

	@Override
	public int hashCode() {
		return Objects.hash(videoID, castMemberID);
	}
}

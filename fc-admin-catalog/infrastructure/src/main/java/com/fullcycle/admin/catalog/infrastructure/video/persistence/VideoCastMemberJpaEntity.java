package com.fullcycle.admin.catalog.infrastructure.video.persistence;

import com.fullcycle.admin.catalog.domain.castmember.CastMemberID;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "videos_cast_members")
@Entity(name = "VideoCastMember")
public class VideoCastMemberJpaEntity {

	@EmbeddedId
	private VideoCastMemberID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("videoId")
	private VideoJpaEntity video;

	public static VideoCastMemberJpaEntity from(final VideoJpaEntity video, CastMemberID castMemberID) {
		return new VideoCastMemberJpaEntity(
		  VideoCastMemberID.from(video.getId(), castMemberID.getValue()),
		  video
		);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		VideoCastMemberJpaEntity that = (VideoCastMemberJpaEntity) o;
		return Objects.equals(id, that.id) && Objects.equals(video, that.video);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, video);
	}
}

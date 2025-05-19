package com.fullcycle.admin.catalog.infrastructure.video.persistence;

import com.fullcycle.admin.catalog.domain.video.AudioVideoMedia;
import com.fullcycle.admin.catalog.domain.video.MediaStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "videos_video_media")
@Entity(name = "AudioMediaVideo")
public class AudioMediaVideoJpaEntity {

	@Id
	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Column(nullable = false)
	private String checksum;

	@Column(nullable = false)
	private String name;

	@Column(name = "file_path", nullable = false)
	private String filePath;

	@Column(name = "encoded_path", nullable = false)
	private String encodedPath;

	@Column(nullable = false)
	private MediaStatus status;

	public static AudioMediaVideoJpaEntity from(final AudioVideoMedia media) {
		return new AudioMediaVideoJpaEntity(
		  media.getChecksum(),
		  media.getName(),
		  media.getRawLocation(),
		  media.getEncodedLocation(),
		  media.getStatus()
		);
	}

	public AudioVideoMedia toDomain() {
		return AudioVideoMedia.with(
		  getChecksum(),
		  getName(),
		  getFilePath(),
		  getEncodedPath(),
		  getStatus()
		);
	}
}

package com.fullcycle.admin.catalog.infrastructure.video.persistence;

import com.fullcycle.admin.catalog.domain.video.ImageMedia;
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
@Table(name = "videos_image_media")
@Entity(name = "ImageMedia")
public class ImageMediaJpaEntity {

	@Id
	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Column(nullable = false)
	private String checksum;

	@Column(nullable = false)
	private String name;

	@Column(name = "file_path", nullable = false)
	private String filePath;

	public static ImageMediaJpaEntity from(final ImageMedia media) {
		return new ImageMediaJpaEntity(
				media.getChecksum(),
				media.getName(),
				media.getLocation()
		);
	}

	public ImageMedia toDomain() {
		return ImageMedia.with(
			getChecksum(),
			getName(),
			getFilePath()
		);
	}
}

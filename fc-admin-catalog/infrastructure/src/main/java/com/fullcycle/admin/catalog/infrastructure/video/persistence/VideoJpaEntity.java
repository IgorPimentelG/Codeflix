package com.fullcycle.admin.catalog.infrastructure.video.persistence;

import com.fullcycle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.genre.GenreID;
import com.fullcycle.admin.catalog.domain.video.Rating;
import com.fullcycle.admin.catalog.domain.video.Video;
import com.fullcycle.admin.catalog.domain.video.VideoID;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.time.Year;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Table(name = "videos")
@Entity(name = "Video")
public class VideoJpaEntity {

	@Id
	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Column(nullable = false)
	private UUID id;

	@Column(nullable = false)
	private String title;

	@Column(length = 4000)
	private String description;

	@Column(name = "year_launched")
	private int yearLaunched;

	@Column(name = "opened")
	private boolean opened;

	@Column(name = "published")
	private boolean published;

	@Enumerated(EnumType.STRING)
	private Rating rating;

	@Column(precision = 2)
	private double duration;

	@Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
	private Instant createdAt;

	@Column(name = "updated_at", columnDefinition = "DATETIME(6)")
	private Instant updatedAt;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "video_id")
	private AudioMediaVideoJpaEntity video;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "trailer_id")
	private AudioMediaVideoJpaEntity trailer;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "banner_id")
	private ImageMediaJpaEntity banner;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "thumbnail_id")
	private ImageMediaJpaEntity thumbnail;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "thumbnail_half_id")
	private ImageMediaJpaEntity thumbnailHalf;

	@OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<VideoCategoryJpaEntity> categories;

	@OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<VideoGenreJpaEntity> genres;

	@OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<VideoCastMemberJpaEntity> castMembers;

	public VideoJpaEntity(
	  final UUID id,
	  final String title,
	  final String description,
	  final int yearLaunched,
	  final boolean opened,
	  final boolean published,
	  final Rating rating,
	  final double duration,
	  final Instant createdAt,
	  final Instant updatedAt,
	  final AudioMediaVideoJpaEntity video,
	  final AudioMediaVideoJpaEntity trailer,
	  final ImageMediaJpaEntity banner,
	  final ImageMediaJpaEntity thumbnail,
	  final ImageMediaJpaEntity thumbnailHalf
	) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.yearLaunched = yearLaunched;
		this.opened = opened;
		this.published = published;
		this.rating = rating;
		this.duration = duration;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.video = video;
		this.trailer = trailer;
		this.banner = banner;
		this.thumbnail = thumbnail;
		this.thumbnailHalf = thumbnailHalf;
		this.categories = new HashSet<>();
		this.genres = new HashSet<>();
		this.castMembers = new HashSet<>();
	}

	public static VideoJpaEntity from(final Video video) {
		final var entity = new VideoJpaEntity(
		  video.getId().getValue(),
		  video.getTitle(),
		  video.getDescription(),
		  video.getLaunchedAt().getValue(),
		  video.isOpened(),
		  video.isPublished(),
		  video.getRating(),
		  video.getDuration(),
		  video.getCreatedAt(),
		  video.getUpdatedAt(),
		  video.getVideo().map(AudioMediaVideoJpaEntity::from).orElse(null),
		  video.getTrailer().map(AudioMediaVideoJpaEntity::from).orElse(null),
		  video.getBanner().map(ImageMediaJpaEntity::from).orElse(null),
		  video.getThumbnail().map(ImageMediaJpaEntity::from).orElse(null),
		  video.getThumbnailHalf().map(ImageMediaJpaEntity::from).orElse(null)
		);

		video.getCategories().forEach(entity::addCategory);
		video.getGenres().forEach(entity::addGenre);
		video.getCastMembers().forEach(entity::addCastMembers);

		return entity;
	}

	public Video toAggregate() {
		return Video.with(
		  VideoID.from(getId()),
		  getTitle(),
		  getDescription(),
		  Year.of(getYearLaunched()),
		  getDuration(),
		  getRating(),
		  isOpened(),
		  isPublished(),
		  Optional.ofNullable(getBanner()).map(ImageMediaJpaEntity::toDomain).orElse(null),
		  Optional.ofNullable(getThumbnail()).map(ImageMediaJpaEntity::toDomain).orElse(null),
		  Optional.ofNullable(getThumbnailHalf()).map(ImageMediaJpaEntity::toDomain).orElse(null),
		  Optional.ofNullable(getTrailer()).map(AudioMediaVideoJpaEntity::toDomain).orElse(null),
		  Optional.ofNullable(getVideo()).map(AudioMediaVideoJpaEntity::toDomain).orElse(null),
		  getCategories().stream().map(it -> CategoryID.from(it.getId().getCategoryID())).collect(Collectors.toSet()),
		  getGenres().stream().map(it -> GenreID.from(it.getId().getGenreID())).collect(Collectors.toSet()),
		  getCastMembers().stream().map(it -> CastMemberID.from(it.getId().getCastMemberID())).collect(Collectors.toSet()),
		  getCreatedAt(),
		  getUpdatedAt()
		);
	}

	public void addCategory(final CategoryID id) {
		categories.add(VideoCategoryJpaEntity.from(this, id));
	}

	public void addGenre(final GenreID id) {
		genres.add(VideoGenreJpaEntity.from(this, id));
	}

	public void addCastMembers(final CastMemberID id) {
		castMembers.add(VideoCastMemberJpaEntity.from(this, id));
	}
}

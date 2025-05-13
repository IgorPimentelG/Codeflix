package com.fullcycle.admin.catalog.domain.video;

import com.fullcycle.admin.catalog.domain.AggregateRoot;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.genre.GenreID;
import com.fullcycle.admin.catalog.domain.validation.ValidationHandler;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.Year;
import java.util.Optional;
import java.util.Set;

@Getter
@Setter
public class Video extends AggregateRoot<VideoID> {

    private String title;
    private String description;
    private Year launchedAt;
    private double duration;
    private Rating rating;

    private boolean opened;
    private boolean published;

    private Instant createdAt;
    private Instant updatedAt;

    private ImageMedia banner;
    private ImageMedia thumbnail;
    private ImageMedia thumbnailHalf;

    private AudioVideoMedia trailer;
    private AudioVideoMedia video;

    private Set<CategoryID> categories;
    private Set<GenreID> genres;
    private Set<CastMemberID> castMembers;

    protected Video(
      final VideoID id,
      final String title,
      final String description,
      final Year launchedAt,
      final double duration,
      final Rating rating,
      final boolean opened,
      final boolean published,
      final ImageMedia banner,
      final ImageMedia thumbnail,
      final ImageMedia thumbnailHalf,
      final AudioVideoMedia trailer,
      final AudioVideoMedia video,
      final Set<CategoryID> categories,
      final Set<GenreID> genres,
      final Set<CastMemberID> castMembers,
      final Instant createdAt,
      final Instant updatedAt
    ) {
        super(id);
        this.title = title;
        this.description = description;
        this.launchedAt = launchedAt;
        this.duration = duration;
        this.rating = rating;
        this.opened = opened;
        this.published = published;
        this.banner = banner;
        this.thumbnail = thumbnail;
        this.thumbnailHalf = thumbnailHalf;
        this.trailer = trailer;
        this.video = video;
        this.categories = categories;
        this.genres = genres;
        this.castMembers = castMembers;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new VideoValidator(this, handler).validate();
    }

    public static Video newVideo(
      final String title,
      final String description,
      final Year launchedAt,
      final double duration,
      final Rating rating,
      final boolean opened,
      final boolean published,
      final Set<CategoryID> categories,
      final Set<GenreID> genres,
      final Set<CastMemberID> castMembers
    ) {
        return new Video(
          VideoID.unique(),
            title,
            description,
            launchedAt,
            duration,
            rating,
            opened,
            published,
            null,
            null,
            null,
            null,
            null,
            categories,
            genres,
            castMembers,
            Instant.now(),
            null
        );
    }

    public static Video with(final Video video) {
        return new Video(
         video.getId(),
         video.getTitle(),
         video.getDescription(),
         video.getLaunchedAt(),
         video.getDuration(),
         video.getRating(),
         video.isOpened(),
         video.isPublished(),
         video.getBanner().orElse(null),
         video.getThumbnail().orElse(null),
         video.getThumbnailHalf().orElse(null),
         video.getTrailer().orElse(null),
         video.getVideo().orElse(null),
         Set.copyOf(video.getCategories()),
         Set.copyOf(video.getGenres()),
         Set.copyOf(video.getCastMembers()),
         video.getCreatedAt(),
         video.getUpdatedAt()
        );
    }

    public Video update(
      final String title,
      final String description,
      final Year launchedAt,
      final double duration,
      final Rating rating,
      final boolean opened,
      final boolean published,
      final Set<CategoryID> categories,
      final Set<GenreID> genres,
      final Set<CastMemberID> castMembers
    ) {
        this.title = title;
        this.description = description;
        this.launchedAt = launchedAt;
        this.duration = duration;
        this.rating = rating;
        this.opened = opened;
        this.published = published;

        this.categories = Set.copyOf(categories);
        this.genres = Set.copyOf(genres);
        this.castMembers = Set.copyOf(castMembers);

        this.updatedAt = Instant.now();

        return this;
    }

    public Video setVideo(final AudioVideoMedia video) {
        this.video = video;
        this.updatedAt = Instant.now();
        return this;
    }

    public Video setTrailer(final AudioVideoMedia trailer) {
        this.trailer = trailer;
        this.updatedAt = Instant.now();
        return this;
    }

    public Video setBanner(final ImageMedia banner) {
        this.banner = banner;
        this.updatedAt = Instant.now();
        return this;
    }

    public Video setThumbnail(final ImageMedia thumbnail) {
        this.thumbnail = thumbnail;
        this.updatedAt = Instant.now();
        return this;
    }

    public Video setThumbnailHalf(final ImageMedia thumbnailHalf) {
        this.thumbnailHalf = thumbnailHalf;
        this.updatedAt = Instant.now();
        return this;
    }

    public Optional<ImageMedia> getBanner() {
        return Optional.ofNullable(banner);
    }

    public Optional<ImageMedia> getThumbnail() {
        return Optional.ofNullable(thumbnail);
    }

    public Optional<ImageMedia> getThumbnailHalf() {
        return Optional.ofNullable(thumbnailHalf);
    }

    public Optional<AudioVideoMedia> getTrailer() {
        return Optional.ofNullable(trailer);
    }

    public Optional<AudioVideoMedia> getVideo() {
        return Optional.ofNullable(video);
    }

    public Set<CategoryID> getCategories() {
        return categories != null ? Set.copyOf(categories) : Set.of();
    }

    public Set<GenreID> getGenres() {
        return genres != null ? Set.copyOf(genres) : Set.of();
    }

    public Set<CastMemberID> getCastMembers() {
        return castMembers != null ? Set.copyOf(castMembers) : Set.of();
    }
}

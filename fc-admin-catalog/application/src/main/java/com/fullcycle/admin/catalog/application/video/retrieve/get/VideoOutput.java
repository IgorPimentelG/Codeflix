package com.fullcycle.admin.catalog.application.video.retrieve.get;

import com.fullcycle.admin.catalog.domain.utils.CollectionUtils;
import com.fullcycle.admin.catalog.domain.video.AudioVideoMedia;
import com.fullcycle.admin.catalog.domain.video.ImageMedia;
import com.fullcycle.admin.catalog.domain.video.Video;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record VideoOutput(
  UUID id,
  String title,
  String description,
  Integer launchedAt,
  Double duration,
  Boolean opened,
  Boolean published,
  String rating,
  Set<String> categories,
  Set<String> genres,
  Set<String> members,
  AudioVideoMedia video,
  AudioVideoMedia trailer,
  ImageMedia banner,
  ImageMedia thumbnail,
  ImageMedia thumbnailHalf,
  Instant createdAt,
  Instant updatedAt
) {
	public static VideoOutput from(final Video video) {
		return new VideoOutput(
		  video.getId().getValue(),
		  video.getTitle(),
		  video.getDescription(),
		  video.getLaunchedAt().getValue(),
		  video.getDuration(),
		  video.isOpened(),
		  video.isPublished(),
		  video.getRating().name(),
		  CollectionUtils.mapTo(video.getCategories(), (identifier) -> identifier.getValue().toString()),
		  CollectionUtils.mapTo(video.getGenres(), (identifier) -> identifier.getValue().toString()),
		  CollectionUtils.mapTo(video.getCastMembers(), (identifier) -> identifier.getValue().toString()),
		  video.getVideo().orElse(null),
		  video.getTrailer().orElse(null),
		  video.getBanner().orElse(null),
		  video.getThumbnail().orElse(null),
		  video.getThumbnailHalf().orElse(null),
		  video.getCreatedAt(),
		  video.getUpdatedAt()
		);
	}
}

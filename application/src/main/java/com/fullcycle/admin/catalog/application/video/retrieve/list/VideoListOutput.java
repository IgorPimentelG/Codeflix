package com.fullcycle.admin.catalog.application.video.retrieve.list;

import com.fullcycle.admin.catalog.domain.video.Video;
import com.fullcycle.admin.catalog.domain.video.VideoPreview;

import java.time.Instant;

public record VideoListOutput(
  String id,
  String title,
  String description,
  Instant createdAt,
  Instant updatedAt
) {

	public static VideoListOutput from(final VideoPreview video) {
		return new VideoListOutput(
		  video.id().toString(),
		  video.title(),
		  video.description(),
		  video.createdAt(),
		  video.updatedAt()
		);
	}
}

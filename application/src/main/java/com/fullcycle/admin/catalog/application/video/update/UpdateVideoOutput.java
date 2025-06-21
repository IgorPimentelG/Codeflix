package com.fullcycle.admin.catalog.application.video.update;

import com.fullcycle.admin.catalog.domain.video.Video;

import java.util.UUID;

public record UpdateVideoOutput(UUID id) {

	public static UpdateVideoOutput from(final Video video) {
		return new UpdateVideoOutput(video.getId().getValue());
	}
}

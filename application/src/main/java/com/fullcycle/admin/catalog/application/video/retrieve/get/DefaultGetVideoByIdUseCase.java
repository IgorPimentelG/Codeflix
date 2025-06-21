package com.fullcycle.admin.catalog.application.video.retrieve.get;

import com.fullcycle.admin.catalog.domain.errors.NotFoundException;
import com.fullcycle.admin.catalog.domain.video.Video;
import com.fullcycle.admin.catalog.domain.video.VideoGateway;
import com.fullcycle.admin.catalog.domain.video.VideoID;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class DefaultGetVideoByIdUseCase extends GetVideoByIdUseCase{

	private final VideoGateway videoGateway;

	@Override
	public VideoOutput execute(final UUID id) {
		final var videoId = VideoID.from(id);
		return videoGateway.findById(videoId)
		  .map(VideoOutput::from)
		  .orElseThrow(() -> NotFoundException.with(Video.class, videoId));
	}
}

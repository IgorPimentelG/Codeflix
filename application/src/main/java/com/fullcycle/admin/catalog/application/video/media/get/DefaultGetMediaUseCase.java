package com.fullcycle.admin.catalog.application.video.media.get;

import com.fullcycle.admin.catalog.domain.errors.NotFoundException;
import com.fullcycle.admin.catalog.domain.validation.Error;
import com.fullcycle.admin.catalog.domain.video.MediaResourceGateway;
import com.fullcycle.admin.catalog.domain.video.VideoID;
import com.fullcycle.admin.catalog.domain.video.VideoMediaType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultGetMediaUseCase extends GetMediaUseCase {

	private final MediaResourceGateway mediaResourceGateway;

	@Override
	public MediaOutput execute(final GetMediaCommand command) {
		final var id = VideoID.from(command.videoID());
		final var type = VideoMediaType.of(command.mediaType())
		  .orElseThrow(() -> typeNotFound(command.mediaType()));

		final var resource = mediaResourceGateway.getResource(id, type)
		  .orElseThrow(() -> notFound(command.videoID(), command.mediaType()));

		return MediaOutput.with(resource);
	}

	private NotFoundException notFound(final String id, final String type) {
		return NotFoundException.with(new Error("Resource %s not found for video %s".formatted(id, type)));
	}

	private NotFoundException typeNotFound(final String type) {
		return NotFoundException.with(new Error("Media type %s doesn't exists".formatted(type)));
	}
}

package com.fullcycle.admin.catalog.application.video.media.upload;

import com.fullcycle.admin.catalog.domain.errors.NotFoundException;
import com.fullcycle.admin.catalog.domain.video.MediaResourceGateway;
import com.fullcycle.admin.catalog.domain.video.Video;
import com.fullcycle.admin.catalog.domain.video.VideoGateway;
import com.fullcycle.admin.catalog.domain.video.VideoID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultUploadMediaUseCase extends UploadMediaUseCase {

	private final VideoGateway videoGateway;
	private final MediaResourceGateway mediaResourceGateway;

	@Override
	public UploadMediaOutput execute(final UploadMediaCommand command) {
		final var id = VideoID.from(command.videoId());
		final var resource = command.resource();

		final var video = videoGateway.findById(id).orElseThrow(() -> notFound(id));

		switch (resource.type()) {
			case VIDEO -> video.setVideo(mediaResourceGateway.storeAudioVideo(id, resource));
			case TRAILER -> video.setTrailer(mediaResourceGateway.storeAudioVideo(id, resource));
			case BANNER -> video.setBanner(mediaResourceGateway.storeImage(id, resource));
			case THUMBNAIL -> video.setThumbnail(mediaResourceGateway.storeImage(id, resource));
			case THUMBNAIL_HALF -> video.setThumbnailHalf(mediaResourceGateway.storeImage(id, resource));
		}

		return UploadMediaOutput.with(videoGateway.update(video), resource.type());
	}

	private NotFoundException notFound(final VideoID id) {
		return NotFoundException.with(Video.class, id);
	}
}

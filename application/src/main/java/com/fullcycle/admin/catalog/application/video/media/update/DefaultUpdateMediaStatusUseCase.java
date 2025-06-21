package com.fullcycle.admin.catalog.application.video.media.update;

import com.fullcycle.admin.catalog.domain.errors.NotFoundException;
import com.fullcycle.admin.catalog.domain.video.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultUpdateMediaStatusUseCase extends UpdateMediaStatusUseCase {

	private final VideoGateway videoGateway;

	@Override
	public void execute(final UpdateMediaStatusCommand command) {
		final var id = VideoID.from(command.videoID());
		final var checksum = command.checksum();
		final var folder = command.folder();
		final var filename = command.filename();

		final var video = videoGateway.findById(id).orElseThrow(() -> notFound(id));

		final var encodedPath = "%s/%s".formatted(folder, filename);

		if (matches(checksum, video.getVideo().orElse(null))) {
			updateVideo(VideoMediaType.VIDEO, command.status(), video, encodedPath);
		} else if (matches(checksum, video.getTrailer().orElse(null))) {
			updateVideo(VideoMediaType.TRAILER, command.status(), video, encodedPath);
		}
	}

	private void updateVideo(final VideoMediaType type, final MediaStatus status, final Video video, final String encodedPath) {
		switch (status) {
			case PENDING -> {}
			case PROCESSING ->  video.processing(type);
			case COMPLETED -> video.completed(type, encodedPath);
		}

		videoGateway.update(video);
	}

	private boolean matches(final String checksum, final AudioVideoMedia media) {
		if (media == null) {
			return false;
		}

		return media.getChecksum().equals(checksum);
	}

	private NotFoundException notFound(final VideoID id) {
		return NotFoundException.with(Video.class, id);
	}
}

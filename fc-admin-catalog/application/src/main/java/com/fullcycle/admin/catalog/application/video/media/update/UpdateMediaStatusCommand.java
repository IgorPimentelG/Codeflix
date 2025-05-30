package com.fullcycle.admin.catalog.application.video.media.update;

import com.fullcycle.admin.catalog.domain.video.MediaStatus;

import java.util.UUID;

public record UpdateMediaStatusCommand(
  MediaStatus status,
  UUID videoID,
  String checksum,
  String folder,
  String filename
) {
	public static UpdateMediaStatusCommand with(final MediaStatus status, final UUID videoID, final String checksum, final String folder, final String filename) {
		return new UpdateMediaStatusCommand(status, videoID, checksum, folder, filename);
	}
}

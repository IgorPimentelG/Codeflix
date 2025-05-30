package com.fullcycle.admin.catalog.application.video.media.upload;

import com.fullcycle.admin.catalog.domain.video.VideoResource;

public record UploadMediaCommand(String videoId, VideoResource resource) {

	public static UploadMediaCommand with(final String videoId, final VideoResource resource) {
		return new UploadMediaCommand(videoId, resource);
	}
}

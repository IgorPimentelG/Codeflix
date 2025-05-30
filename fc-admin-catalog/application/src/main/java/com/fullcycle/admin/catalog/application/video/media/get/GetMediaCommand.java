package com.fullcycle.admin.catalog.application.video.media.get;

public record GetMediaCommand(String videoID, String mediaType) {

	public static GetMediaCommand with(final String videoID, final String mediaType) {
		return new GetMediaCommand(videoID, mediaType);
	}
}

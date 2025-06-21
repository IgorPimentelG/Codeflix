package com.fullcycle.admin.catalog.application.video.media.get;

import com.fullcycle.admin.catalog.domain.resource.Resource;

public record MediaOutput(String name, String checksum, String contentType, byte[] content) {

	public static MediaOutput with(Resource resource) {
		return new MediaOutput(resource.name(), resource.checksum(), resource.contentType(), resource.content());
	}
}

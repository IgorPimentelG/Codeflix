package com.fullcycle.admin.catalog.infrastructure.video;

import com.fullcycle.admin.catalog.domain.resource.Resource;
import com.fullcycle.admin.catalog.domain.video.*;
import com.fullcycle.admin.catalog.infrastructure.configuration.properties.storage.StorageProperties;
import com.fullcycle.admin.catalog.infrastructure.service.StorageService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class VideoMediaResourceGateway implements MediaResourceGateway {

	private final String filenamePattern;
	private final String locationPattern;
	private final StorageService storageService;

	public VideoMediaResourceGateway(final StorageProperties props, final StorageService storageService) {
		this.filenamePattern = props.getFilenamePattern();
		this.locationPattern = props.getLocationPattern();
		this.storageService = storageService;
	}

	@Override
	public AudioVideoMedia storeAudioVideo(final VideoID id, final VideoResource resource) {
		final var filePath = filePath(id, resource.type());
		final var content = resource.resource();
		store(filePath, content);
		return AudioVideoMedia.with(content.checksum(), content.name(), filePath);
	}

	@Override
	public ImageMedia storeImage(VideoID id, VideoResource resource) {
		final var filePath = filePath(id, resource.type());
		final var content = resource.resource();
		store(filePath, content);
		return ImageMedia.with(content.checksum(), content.name(), filePath);
	}

	@Override
	public Optional<Resource> getResource(VideoID id, VideoMediaType type) {
		return storageService.get(filePath(id, type));
	}

	@Override
	public void clearResources(VideoID id) {
		final var ids = storageService.list(folder(id));
		storageService.deleteAll(ids);
	}

	private void store(final String filePath, final Resource content) {
		storageService.store(filePath, content);
	}

	private String filePath(final VideoID id, final VideoMediaType type) {
		return folder(id).concat("/").concat(filename(type));
	}

	private String filename(final VideoMediaType type) {
		return filenamePattern.replace("{type}", type.name());
	}

	private String folder(final VideoID id) {
		return locationPattern.replace("{videoId}", id.toString());
	}
}

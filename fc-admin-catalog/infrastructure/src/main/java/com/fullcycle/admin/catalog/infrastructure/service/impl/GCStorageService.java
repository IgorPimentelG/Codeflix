package com.fullcycle.admin.catalog.infrastructure.service.impl;

import com.fullcycle.admin.catalog.domain.resource.Resource;
import com.fullcycle.admin.catalog.infrastructure.service.StorageService;
import com.fullcycle.admin.catalog.infrastructure.utils.ChecksumUtils;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
public class GCStorageService implements StorageService {

	private final String bucket;
	private final Storage storage;

	@Override
	public void store(String name, Resource resource) {
		final var blobInfo = BlobInfo.newBuilder(bucket, name)
		  .setContentType(resource.contentType())
		  .setCrc32cFromHexString(resource.checksum())
		  .build();
		storage.create(blobInfo, resource.content());
	}

	@Override
	public void deleteAll(Collection<String> names) {
		final var blobs = names.stream().map(name -> BlobId.of(bucket, name)).toList();
		storage.delete(blobs);
	}

	@Override
	public List<String> list(String prefix) {
		final var blobs = storage.list(bucket, Storage.BlobListOption.prefix(prefix));
		return StreamSupport.stream(blobs.iterateAll().spliterator(), false)
		  .map(BlobInfo::getBlobId)
		  .map(BlobId::getName)
		  .toList();
	}

	@Override
	public Optional<Resource> get(String name) {
		return Optional.ofNullable(storage.get(bucket, name))
		  .map(blob -> Resource.with(
		    ChecksumUtils.generate(blob.getContent()),
			blob.getContent(),
		    blob.getContentType(),
		    name
		  ));
	}
}

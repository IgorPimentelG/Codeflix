package com.fullcycle.admin.catalog.services.impl;

import com.fullcycle.admin.catalog.Fixture;
import com.fullcycle.admin.catalog.domain.resource.Resource;
import com.fullcycle.admin.catalog.domain.video.VideoMediaType;
import com.fullcycle.admin.catalog.infrastructure.service.impl.GCStorageService;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;

import static com.google.cloud.storage.Storage.BlobListOption.prefix;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GCStorageServiceTest {

	private GCStorageService target;

	private Storage storage;

	private final String bucket = "test-bucket";

	@Before
	public void setUp() {
		storage = Mockito.mock(Storage.class);
		target = new GCStorageService(bucket, storage);
	}

	@Test
	public void givenValidResource_whenCallsStore_shouldStoreIt() {
		final var expectedName = Fixture.name();
		final var expectedSource = Fixture.Videos.resource(VideoMediaType.VIDEO);

		final var blob = mockBlob(expectedName, expectedSource);
		doReturn(blob).when(storage).create(any(BlobInfo.class), any());

		target.store(expectedName, expectedSource);

		final var captor = ArgumentCaptor.forClass(BlobInfo.class);
		verify(storage, times(1)).create(captor.capture(), eq(expectedSource.content()));

		final var actualBlob = captor.getValue();
		assertEquals(bucket, actualBlob.getBlobId().getBucket());
		assertEquals(expectedName, actualBlob.getBlobId().getName());
		assertEquals(expectedName, actualBlob.getName());
		assertEquals(expectedSource.checksum(), actualBlob.getCrc32cToHexString());
		assertEquals(expectedSource.contentType(), actualBlob.getContentType());
	}

	@Test
	public void givenValidResource_whenCallsGet_shouldRetrieveIt() {
		final var expectedName = Fixture.name();
		final var expectedSource = Fixture.Videos.resource(VideoMediaType.VIDEO);

		final var blob = mockBlob(expectedName, expectedSource);
		doReturn(blob).when(storage).get(anyString(), anyString());

		final var resource = target.get(expectedName).get();

		verify(storage, times(1)).get(eq(bucket), eq(expectedName));
		assertEquals(expectedSource.checksum(), resource.checksum());
		assertEquals(expectedSource.contentType(), resource.contentType());
	}

	@Test
	public void givenValidResource_whenCallsGet_shouldBeEmpty() {
		final var expectedName = Fixture.name();
		final var expectedSource = Fixture.Videos.resource(VideoMediaType.VIDEO);

		final var blob = mockBlob(expectedName, expectedSource);
		doReturn(blob).when(storage).get(anyString(), anyString());

		final var resource = target.get(expectedName).get();

		verify(storage, times(1)).get(eq(bucket), eq(expectedName));
		assertEquals(expectedSource.checksum(), resource.checksum());
		assertEquals(expectedSource.contentType(), resource.contentType());
	}

	@Test
	public void givenInvalidResource_whenCallsGet_shouldRetrieveEmpty() {
		final var expectedName = Fixture.name();

		doReturn(null).when(storage).get(anyString(), anyString());

		final var resource = target.get(expectedName);

		assertTrue(resource.isEmpty());
	}

	@Test
	public void givenValidPrefix_whenList_shouldRetrieveAll() {
		final var expectedPrefix = "videos_";
		final var expectedName1 = expectedPrefix + UUID.randomUUID().toString();
		final var expectedName2 = expectedPrefix + UUID.randomUUID().toString();
		final var expectedSource1 = Fixture.Videos.resource(VideoMediaType.VIDEO);
		final var expectedSource2 = Fixture.Videos.resource(VideoMediaType.VIDEO);

		final var expectedResources = List.of(expectedSource1, expectedSource2);
		final var blob1 = mockBlob(expectedName1, expectedSource1);
		final var blob2 = mockBlob(expectedName2, expectedSource2);
		final var page = Mockito.mock(Page.class);

		doReturn(List.of(blob1, blob2)).when(page).iterateAll();
		doReturn(page).when(storage).list(anyString(), any());

		final var resources = target.list(expectedPrefix);

		verify(storage, times(1)).list(eq(bucket), eq(prefix(expectedPrefix)));
		assertEquals(expectedResources.size(), resources.size());
	}

	@Test
	public void givenValidNames_whenCallsDelete_shouldDeleteAll() {
		final var expectedPrefix = "videos_";
		final var expectedName1 = expectedPrefix + UUID.randomUUID().toString();
		final var expectedName2 = expectedPrefix + UUID.randomUUID().toString();

		final var expectedResources = List.of(expectedName1, expectedName2);

		target.deleteAll(expectedResources);

		final var captor = ArgumentCaptor.forClass(List.class);

		verify(storage, times(1)).delete(captor.capture());

		final var resources = ((List<BlobId>) captor.getValue()).stream()
			.map(BlobId::getName)
			  .toList();
		assertEquals(expectedResources.size(), resources.size());
	}

	private Blob mockBlob(final String name, final Resource resource) {
		final var blob = Mockito.mock(Blob.class);
		when(blob.getBlobId()).thenReturn(BlobId.of(bucket, name));
		when(blob.getCrc32cToHexString()).thenReturn(resource.checksum());
		when(blob.getContentType()).thenReturn(resource.contentType());
		when(blob.getContent()).thenReturn(resource.content());
		when(blob.getName()).thenReturn(resource.name());
		return blob;
	}
}

package com.fullcycle.admin.catalog.infrastructure.video;

import com.fullcycle.admin.catalog.Fixture;
import com.fullcycle.admin.catalog.IntegrationTest;
import com.fullcycle.admin.catalog.domain.video.*;
import com.fullcycle.admin.catalog.infrastructure.service.StorageService;
import com.fullcycle.admin.catalog.infrastructure.service.local.InMemoryStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.wildfly.common.Assert.assertNotNull;
import static org.wildfly.common.Assert.assertTrue;


@IntegrationTest
class VideoMediaResourceGatewayTest {

	@Autowired
	private StorageService storageService;

	@Autowired
	private MediaResourceGateway mediaResourceGateway;

	@BeforeEach
	public void setUp() {
		storageService().reset();
	}

	@Test
	public void givenValidResource_whenCallsStorageAudioVideo_shouldStoreIt() {
		final var expectedStatus = MediaStatus.PENDING;
		final var expectedVideoID = VideoID.unique();
		final var expectedType = VideoMediaType.VIDEO;
		final var expectedResource = Fixture.Videos.resource(expectedType);
		final var expectedLocation = "videoId-%s/type-%s".formatted(expectedVideoID, expectedType);

		final var media = mediaResourceGateway.storeAudioVideo(expectedVideoID, VideoResource.with(expectedResource, expectedType));

		assertNotNull(media);
		assertEquals(expectedLocation, media.getRawLocation());
		assertEquals(expectedResource.checksum(), media.getChecksum());
		assertEquals(expectedResource.name(), media.getName());
		assertEquals(expectedStatus, media.getStatus());

		final var stored = storageService().get(expectedLocation);
		assertEquals(expectedResource.checksum(), stored.get().checksum());
	}

	@Test
	public void givenValidResource_whenCallsStorageImage_shouldStoreIt() {
		final var expectedVideoID = VideoID.unique();
		final var expectedType = VideoMediaType.BANNER;
		final var expectedResource = Fixture.Videos.resource(expectedType);
		final var expectedLocation = "videoId-%s/type-%s".formatted(expectedVideoID, expectedType);

		final var media = mediaResourceGateway.storeImage(expectedVideoID, VideoResource.with(expectedResource, expectedType));

		assertNotNull(media);
		assertEquals(expectedLocation, media.getLocation());
		assertEquals(expectedResource.checksum(), media.getChecksum());
		assertEquals(expectedResource.name(), media.getName());

		final var stored = storageService().get(expectedLocation);
		assertEquals(expectedResource.checksum(), stored.get().checksum());
	}

	@Test
	public void givenValidResource_whenCallsCallsClearResources_shouldDeleteAll() {
		final var videoOne = VideoID.unique();
		final var videoTwo = VideoID.unique();

		final var toBeDeleted = new ArrayList<String>();
		toBeDeleted.add("videoId-%s/type-%s".formatted(videoOne.toString(), VideoMediaType.VIDEO.name()));
		toBeDeleted.add("videoId-%s/type-%s".formatted(videoOne.toString(), VideoMediaType.BANNER.name()));
		toBeDeleted.add("videoId-%s/type-%s".formatted(videoOne.toString(), VideoMediaType.TRAILER.name()));

		final var expectedValues = new ArrayList<String>();
		expectedValues.add("videoId-%s/type-%s".formatted(videoTwo.toString(), VideoMediaType.VIDEO.name()));
		expectedValues.add("videoId-%s/type-%s".formatted(videoTwo.toString(), VideoMediaType.BANNER.name()));

		toBeDeleted.forEach(id -> storageService().store(id, Fixture.Videos.resource(Fixture.Videos.mediaType())));
		expectedValues.forEach(id -> storageService().store(id, Fixture.Videos.resource(Fixture.Videos.mediaType())));

		assertEquals(5, storageService().storage().size());

		mediaResourceGateway.clearResources(videoOne);

		assertEquals(2, storageService().storage().size());
		final var keys = storageService().storage().keySet();
		assertTrue(expectedValues.size() == keys.size() && keys.containsAll(expectedValues));
	}


	private InMemoryStorageService storageService() {
		return (InMemoryStorageService) storageService;
	}
}
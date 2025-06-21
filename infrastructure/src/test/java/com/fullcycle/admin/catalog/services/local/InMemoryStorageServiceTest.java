package com.fullcycle.admin.catalog.services.local;

import com.fullcycle.admin.catalog.Fixture;
import com.fullcycle.admin.catalog.domain.video.VideoMediaType;
import com.fullcycle.admin.catalog.infrastructure.service.local.InMemoryStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemoryStorageServiceTest {

	private InMemoryStorageService target = new InMemoryStorageService();

	@BeforeEach
	public void setUp() {
		target.reset();
	}

	@Test
	public void givenValidResource_whenCallsStore_shouldStoreIt() {
		final var expectedName = Fixture.name();
		final var expectedSource = Fixture.Videos.resource(VideoMediaType.VIDEO);

		target.store(expectedName, expectedSource);

		assertEquals(expectedSource, target.storage().get(expectedName));
	}

	@Test
	public void givenValidResource_whenCallsGet_shouldRetrieveIt() {
		final var expectedName = Fixture.name();
		final var expectedSource = Fixture.Videos.resource(VideoMediaType.VIDEO);

		target.storage().put(expectedName, expectedSource);

		final var resource = target.get(expectedName).get();

		assertEquals(expectedSource, resource);
	}

	@Test
	public void givenValidResource_whenCallsGet_shouldBeEmpty() {
		final var expectedName = Fixture.name();

		final var resource = target.get(expectedName);

		assertTrue(resource.isEmpty());
	}

	@Test
	public void givenValidPrefix_whenList_shouldRetrieveAll() {
		final var expectedNames = List.of(
		  "video_" + UUID.randomUUID().toString(),
		  "video_" + UUID.randomUUID().toString(),
		  "video_" + UUID.randomUUID().toString()
		);

		final var all = new ArrayList<>(expectedNames);
		all.add("image_" + UUID.randomUUID().toString());
		all.forEach(name -> target.storage().put(name, Fixture.Videos.resource(VideoMediaType.VIDEO)));

		assertEquals(4, target.storage().size());

		final var resources = target.list("video");

		assertEquals(expectedNames.size(), resources.size());
	}

	@Test
	public void givenValidNames_whenCallsDelete_shouldDeleteAll() {
		final var expectedNamesToDelete = List.of(
		  "video_" + UUID.randomUUID().toString(),
		  "video_" + UUID.randomUUID().toString(),
		  "video_" + UUID.randomUUID().toString()
		);

		final var expectedNames = List.of(
		  "image_" + UUID.randomUUID().toString(),
		  "image_" + UUID.randomUUID().toString()
		);

		final var all = new ArrayList<>(expectedNames);
		all.addAll(expectedNamesToDelete);
		all.forEach(name -> target.storage().put(name, Fixture.Videos.resource(VideoMediaType.VIDEO)));

		assertEquals(5, target.storage().size());

		target.deleteAll(expectedNamesToDelete);

		assertEquals(expectedNames.size(), target.storage().size());
	}
}

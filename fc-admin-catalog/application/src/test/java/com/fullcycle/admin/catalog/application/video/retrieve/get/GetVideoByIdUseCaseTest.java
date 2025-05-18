package com.fullcycle.admin.catalog.application.video.retrieve.get;

import com.fullcycle.admin.catalog.application.Fixture;
import com.fullcycle.admin.catalog.application.UseCaseTest;
import com.fullcycle.admin.catalog.domain.errors.NotFoundException;
import com.fullcycle.admin.catalog.domain.video.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Year;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GetVideoByIdUseCaseTest extends UseCaseTest {

	@Mock
	private VideoGateway videoGateway;

	@InjectMocks
	private DefaultGetVideoByIdUseCase useCase;

	@Override
	protected List<Object> getMocks() {
		return List.of(videoGateway);
	}

	@Test
	public void givenValidId_whenCallsGetVideo_shouldReturnIt() {
		final var expectedTitle = Fixture.title();
		final var expectedDescription = Fixture.description();
		final var expectedLaunchedAt = Year.of(Fixture.year());
		final var expectedDuration = Fixture.duration();
		final var expectedOpened = Fixture.bool();
		final var expectedPublished = Fixture.bool();
		final var expectedRating = Fixture.Videos.rating();
		final var expectedCategories = Set.of(Fixture.Categories.category().getId());
		final var expectedGenres = Set.of(Fixture.Genres.genre().getId());
		final var expectedCastMembers = Set.of(Fixture.CastMembers.castMember().getId());
		final var expectedVideo = AudioVideoMedia.with(UUID.randomUUID().toString(), "any name", "/raw-location", "/encoded-location", MediaStatus.PENDING);
		final var expectedTrailer = AudioVideoMedia.with(UUID.randomUUID().toString(), "any name", "/raw-location", "/encoded-location", MediaStatus.COMPLETED);
		final var expectedBanner = ImageMedia.with(UUID.randomUUID().toString(), "any name", "/location");
		final var expectedThumb = ImageMedia.with(UUID.randomUUID().toString(), "any name", "/location");
		final var expectedThumbHalf = ImageMedia.with(UUID.randomUUID().toString(), "any name", "/location");

		final var video = Video.newVideo(
		  expectedTitle,
		  expectedDescription,
		  expectedLaunchedAt,
		  expectedDuration,
		  expectedRating,
		  expectedOpened,
		  expectedPublished,
		  expectedCategories,
		  expectedGenres,
		  expectedCastMembers
		)
		  .setVideo(expectedVideo)
		  .setTrailer(expectedTrailer)
		  .setBanner(expectedBanner)
		  .setThumbnail(expectedThumb)
		  .setThumbnailHalf(expectedThumbHalf);

		when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(video)));

		final var output = useCase.execute(video.getId().getValue());

		assertEquals(expectedTitle, output.title());
		assertEquals(expectedDescription, output.description());
		assertEquals(expectedLaunchedAt.getValue(), output.launchedAt());
		assertEquals(expectedDuration, output.duration());
		assertEquals(expectedOpened, output.opened());
		assertEquals(expectedPublished, output.published());
		assertEquals(expectedRating.name(), output.rating());
		assertEquals(asStrings(expectedCategories), output.categories());
		assertEquals(asStrings(expectedGenres), output.genres());
		assertEquals(asStrings(expectedCastMembers), output.members());
		assertEquals(expectedVideo, output.video());
		assertEquals(expectedTrailer, output.trailer());
		assertEquals(expectedBanner, output.banner());
		assertEquals(expectedThumb, output.thumbnail());
		assertEquals(expectedThumbHalf, output.thumbnailHalf());
		assertEquals(video.getCreatedAt(), output.createdAt());
		assertEquals(video.getUpdatedAt(), output.updatedAt());
	}

	@Test
	public void givenInvalidId_whenCallsGetVideo_shouldReturnNotFound() {
		final var expectedId = VideoID.unique();
		final var expectedErrorMessage = "Video with ID %s was not found".formatted(expectedId.getValue().toString());

		when(videoGateway.findById(any())).thenReturn(Optional.empty());

		final var error = assertThrows(NotFoundException.class, () -> useCase.execute(expectedId.getValue()));

		assertEquals(expectedErrorMessage, error.getMessage());
	}
}

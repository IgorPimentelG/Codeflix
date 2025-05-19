package com.fullcycle.admin.catalog.application.video.update;

import com.fullcycle.admin.catalog.application.Fixture;
import com.fullcycle.admin.catalog.application.UseCaseTest;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalog.domain.category.CategoryGateway;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.errors.InternalException;
import com.fullcycle.admin.catalog.domain.errors.NotificationException;
import com.fullcycle.admin.catalog.domain.genre.GenreGateway;
import com.fullcycle.admin.catalog.domain.genre.GenreID;
import com.fullcycle.admin.catalog.domain.video.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class UpdateVideoUseCaseTest extends UseCaseTest  {

	@Mock
	private VideoGateway videoGateway;

	@Mock
	private CategoryGateway categoryGateway;

	@Mock
	private GenreGateway genreGateway;

	@Mock
	private CastMemberGateway castMemberGateway;

	@Mock
	private MediaResourceGateway mediaResourceGateway;

	@InjectMocks
	private DefaultUpdateVideoUseCase useCase;

	@Override
	protected List<Object> getMocks() {
		return List.of(
		  videoGateway,
		  categoryGateway,
		  genreGateway,
		  castMemberGateway,
		  mediaResourceGateway
		);
	}

	@Test
	public void givenValidCommand_whenCallsUpdateVideo_shouldReturnVideoId() {
		final var expectedTitle = Fixture.title();
		final var expectedDescription = Fixture.description();
		final var expectedLaunchedAt = Fixture.year();
		final var expectedDuration = Fixture.duration();
		final var expectedOpened = Fixture.bool();
		final var expectedPublished = Fixture.bool();
		final var expectedRating = Fixture.Videos.rating();
		final var expectedCategories = Set.of(Fixture.Categories.category().getId());
		final var expectedGenres = Set.of(Fixture.Genres.genre().getId());
		final var expectedCastMembers = Set.of(Fixture.CastMembers.castMember().getId());
		final Resource expectedVideo = Fixture.Videos.resource(Resource.Type.VIDEO);
		final Resource expectedTrailer = Fixture.Videos.resource(Resource.Type.TRAILER);
		final Resource expectedBanner = Fixture.Videos.resource(Resource.Type.BANNER);
		final Resource expectedThumb = Fixture.Videos.resource(Resource.Type.THUMBNAIL);
		final Resource expectedThumbHalf = Fixture.Videos.resource(Resource.Type.THUMBNAIL_HALF);

		final var video = Fixture.Videos.video();

		final var command = new UpdateVideoCommand(
		  video.getId().toString(),
		  expectedTitle,
		  expectedDescription,
		  expectedLaunchedAt,
		  expectedDuration,
		  expectedOpened,
		  expectedPublished,
		  expectedRating.getName(),
		  asStrings(expectedCategories),
		  asStrings(expectedGenres),
		  asStrings(expectedCastMembers),
		  expectedVideo,
		  expectedTrailer,
		  expectedBanner,
		  expectedThumb,
		  expectedThumbHalf
		);

		mockImageMedia();
		mockAudioVideoMedia();

		when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(video)));
		when(categoryGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCategories));
		when(genreGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedGenres));
		when(castMemberGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCastMembers));
		when(videoGateway.update(any())).thenAnswer(returnsFirstArg());

		final var output = useCase.execute(command);

		assertNotNull(output);
		assertNotNull(output.id());

		verify(videoGateway).update(argThat(updatedVideo ->
		  Objects.equals(expectedTitle, updatedVideo.getTitle()) &&
			Objects.equals(expectedDescription, updatedVideo.getDescription()) &&
			Objects.equals(expectedLaunchedAt, updatedVideo.getLaunchedAt().getValue()) &&
			Objects.equals(expectedDuration, updatedVideo.getDuration()) &&
			Objects.equals(expectedOpened, updatedVideo.isOpened()) &&
			Objects.equals(expectedPublished, updatedVideo.isPublished()) &&
			Objects.equals(expectedRating, updatedVideo.getRating()) &&
			Objects.equals(expectedCategories, updatedVideo.getCategories()) &&
			Objects.equals(expectedGenres, updatedVideo.getGenres()) &&
			Objects.equals(expectedCastMembers, updatedVideo.getCastMembers()) &&
		    Objects.nonNull(updatedVideo.getCreatedAt()) &&
		    Objects.nonNull(updatedVideo.getUpdatedAt()) &&
		    updatedVideo.getVideo().isPresent() &&
		    updatedVideo.getBanner().isPresent() &&
		    updatedVideo.getTrailer().isPresent() &&
		    updatedVideo.getThumbnail().isPresent() &&
		    updatedVideo.getThumbnailHalf().isPresent()
		));
	}

	@Test
	public void givenValidCommandWithoutRelations_whenCallsUpdateVideo_shouldReturnVideoId() {
		final var expectedTitle = Fixture.title();
		final var expectedDescription = Fixture.description();
		final var expectedLaunchedAt = Fixture.year();
		final var expectedDuration = Fixture.duration();
		final var expectedOpened = Fixture.bool();
		final var expectedPublished = Fixture.bool();
		final var expectedRating = Fixture.Videos.rating();
		final var expectedCategories = Set.<CategoryID>of();
		final var expectedGenres = Set.<GenreID>of();
		final var expectedCastMembers = Set.<CastMemberID>of();
		final Resource expectedVideo = Fixture.Videos.resource(Resource.Type.VIDEO);
		final Resource expectedTrailer = Fixture.Videos.resource(Resource.Type.TRAILER);
		final Resource expectedBanner = Fixture.Videos.resource(Resource.Type.BANNER);
		final Resource expectedThumb = Fixture.Videos.resource(Resource.Type.THUMBNAIL);
		final Resource expectedThumbHalf = Fixture.Videos.resource(Resource.Type.THUMBNAIL_HALF);

		final var video = Fixture.Videos.video();

		final var command = new UpdateVideoCommand(
		  video.getId().toString(),
		  expectedTitle,
		  expectedDescription,
		  expectedLaunchedAt,
		  expectedDuration,
		  expectedOpened,
		  expectedPublished,
		  expectedRating.getName(),
		  asStrings(expectedCategories),
		  asStrings(expectedGenres),
		  asStrings(expectedCastMembers),
		  expectedVideo,
		  expectedTrailer,
		  expectedBanner,
		  expectedThumb,
		  expectedThumbHalf
		);

		when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(video)));
		lenient().when(categoryGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCategories));
		lenient().when(genreGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedGenres));
		lenient().when(castMemberGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCastMembers));
		when(videoGateway.update(any())).thenAnswer(returnsFirstArg());

		mockImageMedia();
		mockAudioVideoMedia();

		final var output = useCase.execute(command);

		assertNotNull(output);
		assertNotNull(output.id());

		verify(videoGateway).update(argThat(updatedVideo ->
		  Objects.equals(expectedTitle, updatedVideo.getTitle()) &&
			Objects.equals(expectedDescription, updatedVideo.getDescription()) &&
			Objects.equals(expectedLaunchedAt, updatedVideo.getLaunchedAt().getValue()) &&
			Objects.equals(expectedDuration, updatedVideo.getDuration()) &&
			Objects.equals(expectedOpened, updatedVideo.isOpened()) &&
			Objects.equals(expectedPublished, updatedVideo.isPublished()) &&
			Objects.equals(expectedRating, updatedVideo.getRating()) &&
			Objects.equals(expectedCategories, updatedVideo.getCategories()) &&
			Objects.equals(expectedGenres, updatedVideo.getGenres()) &&
			Objects.equals(expectedCastMembers, updatedVideo.getCastMembers()) &&
			Objects.nonNull(updatedVideo.getCreatedAt()) &&
			Objects.nonNull(updatedVideo.getUpdatedAt()) &&
			updatedVideo.getVideo().isPresent() &&
			updatedVideo.getBanner().isPresent() &&
			updatedVideo.getTrailer().isPresent() &&
			updatedVideo.getThumbnail().isPresent() &&
			updatedVideo.getThumbnailHalf().isPresent()
		));
	}

	@Test
	public void givenValidCommandWithoutResources_whenCallsUpdateVideo_shouldReturnVideoId() {
		final var expectedTitle = Fixture.title();
		final var expectedDescription = Fixture.description();
		final var expectedLaunchedAt = Fixture.year();
		final var expectedDuration = Fixture.duration();
		final var expectedOpened = Fixture.bool();
		final var expectedPublished = Fixture.bool();
		final var expectedRating = Fixture.Videos.rating();
		final var expectedCategories = Set.of(Fixture.Categories.category().getId());
		final var expectedGenres = Set.of(Fixture.Genres.genre().getId());
		final var expectedCastMembers = Set.of(Fixture.CastMembers.castMember().getId());
		final Resource expectedVideo = null;
		final Resource expectedTrailer = null;
		final Resource expectedBanner = null;
		final Resource expectedThumb = null;
		final Resource expectedThumbHalf = null;

		final var video = Fixture.Videos.video();

		final var command = new UpdateVideoCommand(
		  video.getId().toString(),
		  expectedTitle,
		  expectedDescription,
		  expectedLaunchedAt,
		  expectedDuration,
		  expectedOpened,
		  expectedPublished,
		  expectedRating.getName(),
		  asStrings(expectedCategories),
		  asStrings(expectedGenres),
		  asStrings(expectedCastMembers),
		  expectedVideo,
		  expectedTrailer,
		  expectedBanner,
		  expectedThumb,
		  expectedThumbHalf
		);

		when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(video)));
		when(categoryGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCategories));
		when(genreGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedGenres));
		when(castMemberGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCastMembers));
		when(videoGateway.update(any())).thenAnswer(returnsFirstArg());

		final var output = useCase.execute(command);

		assertNotNull(output);
		assertNotNull(output.id());

		verify(videoGateway).update(argThat(updatedVideo ->
		  Objects.equals(expectedTitle, updatedVideo.getTitle()) &&
			Objects.equals(expectedDescription, updatedVideo.getDescription()) &&
			Objects.equals(expectedLaunchedAt, updatedVideo.getLaunchedAt().getValue()) &&
			Objects.equals(expectedDuration, updatedVideo.getDuration()) &&
			Objects.equals(expectedOpened, updatedVideo.isOpened()) &&
			Objects.equals(expectedPublished, updatedVideo.isPublished()) &&
			Objects.equals(expectedRating, updatedVideo.getRating()) &&
			Objects.equals(expectedCategories, updatedVideo.getCategories()) &&
			Objects.equals(expectedGenres, updatedVideo.getGenres()) &&
			Objects.equals(expectedCastMembers, updatedVideo.getCastMembers()) &&
			Objects.nonNull(updatedVideo.getCreatedAt()) &&
			Objects.nonNull(updatedVideo.getUpdatedAt()) &&
			updatedVideo.getVideo().isEmpty() &&
			updatedVideo.getBanner().isEmpty() &&
			updatedVideo.getTrailer().isEmpty() &&
			updatedVideo.getThumbnail().isEmpty() &&
			updatedVideo.getThumbnailHalf().isEmpty()
		));
	}

	@Test
	public void givenNullTitle_whenCallsUpdateVideo_shouldReturnDomainException() {
		final String expectedTitle = null;
		final var expectedDescription = Fixture.description();
		final var expectedLaunchedAt = Fixture.year();
		final var expectedDuration = Fixture.duration();
		final var expectedOpened = Fixture.bool();
		final var expectedPublished = Fixture.bool();
		final var expectedRating = Fixture.Videos.rating();
		final var expectedCategories = Set.<String>of();
		final var expectedGenres = Set.<String>of();
		final var expectedCastMembers = Set.<String>of();
		final Resource expectedVideo = null;
		final Resource expectedTrailer = null;
		final Resource expectedBanner = null;
		final Resource expectedThumb = null;
		final Resource expectedThumbHalf = null;

		final var expectedErrorCount = 1;
		final var expectedErrorMessage = "Title cannot be null";

		final var video = Fixture.Videos.video();

		final var command = new UpdateVideoCommand(
		  video.getId().toString(),
		  expectedTitle,
		  expectedDescription,
		  expectedLaunchedAt,
		  expectedDuration,
		  expectedOpened,
		  expectedPublished,
		  expectedRating.getName(),
		  expectedCategories,
		  expectedGenres,
		  expectedCastMembers,
		  expectedVideo,
		  expectedTrailer,
		  expectedBanner,
		  expectedThumb,
		  expectedThumbHalf
		);

		when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(video)));
		final var error = assertThrows(NotificationException.class, () -> useCase.execute(command));

		assertNotNull(error);
		assertEquals(expectedErrorCount, error.getErrors().size());
		assertEquals(expectedErrorMessage, error.getErrors().get(0).message());

		verify(videoGateway, times(0)).update(any());
	}

	@Test
	public void givenEmptyTitle_whenCallsUpdateVideo_shouldReturnDomainException() {
		final var expectedTitle = "";
		final var expectedDescription = Fixture.description();
		final var expectedLaunchedAt = Fixture.year();
		final var expectedDuration = Fixture.duration();
		final var expectedOpened = Fixture.bool();
		final var expectedPublished = Fixture.bool();
		final var expectedRating = Fixture.Videos.rating();
		final var expectedCategories = Set.<String>of();
		final var expectedGenres = Set.<String>of();
		final var expectedCastMembers = Set.<String>of();
		final Resource expectedVideo = null;
		final Resource expectedTrailer = null;
		final Resource expectedBanner = null;
		final Resource expectedThumb = null;
		final Resource expectedThumbHalf = null;

		final var expectedErrorCount = 1;
		final var expectedErrorMessage = "Title cannot be empty";

		final var video = Fixture.Videos.video();

		final var command = new UpdateVideoCommand(
		  video.getId().toString(),
		  expectedTitle,
		  expectedDescription,
		  expectedLaunchedAt,
		  expectedDuration,
		  expectedOpened,
		  expectedPublished,
		  expectedRating.getName(),
		  expectedCategories,
		  expectedGenres,
		  expectedCastMembers,
		  expectedVideo,
		  expectedTrailer,
		  expectedBanner,
		  expectedThumb,
		  expectedThumbHalf
		);

		when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(video)));
		final var error = assertThrows(NotificationException.class, () -> useCase.execute(command));

		assertNotNull(error);
		assertEquals(expectedErrorCount, error.getErrors().size());
		assertEquals(expectedErrorMessage, error.getErrors().get(0).message());

		verify(videoGateway, times(0)).update(any());
	}

	@Test
	public void givenNullRating_whenCallsUpdateVideo_shouldReturnDomainException() {
		final var expectedTitle = Fixture.title();
		final var expectedDescription = Fixture.description();
		final var expectedLaunchedAt = Fixture.year();
		final var expectedDuration = Fixture.duration();
		final var expectedOpened = Fixture.bool();
		final var expectedPublished = Fixture.bool();
		final String expectedRating = null;
		final var expectedCategories = Set.<String>of();
		final var expectedGenres = Set.<String>of();
		final var expectedCastMembers = Set.<String>of();
		final Resource expectedVideo = null;
		final Resource expectedTrailer = null;
		final Resource expectedBanner = null;
		final Resource expectedThumb = null;
		final Resource expectedThumbHalf = null;

		final var expectedErrorCount = 1;
		final var expectedErrorMessage = "Rating cannot be null";

		final var video = Fixture.Videos.video();

		final var command = new UpdateVideoCommand(
		  video.getId().toString(),
		  expectedTitle,
		  expectedDescription,
		  expectedLaunchedAt,
		  expectedDuration,
		  expectedOpened,
		  expectedPublished,
		  expectedRating,
		  expectedCategories,
		  expectedGenres,
		  expectedCastMembers,
		  expectedVideo,
		  expectedTrailer,
		  expectedBanner,
		  expectedThumb,
		  expectedThumbHalf
		);

		when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(video)));
		final var error = assertThrows(NotificationException.class, () -> useCase.execute(command));

		assertNotNull(error);
		assertEquals(expectedErrorCount, error.getErrors().size());
		assertEquals(expectedErrorMessage, error.getErrors().get(0).message());

		verify(videoGateway, times(0)).update(any());
	}

	@Test
	public void givenInvalidRating_whenCallsUpdateVideo_shouldReturnDomainException() {
		final var expectedTitle = Fixture.title();
		final var expectedDescription = Fixture.description();
		final var expectedLaunchedAt = Fixture.year();
		final var expectedDuration = Fixture.duration();
		final var expectedOpened = Fixture.bool();
		final var expectedPublished = Fixture.bool();
		final var expectedRating = "any";
		final var expectedCategories = Set.<String>of();
		final var expectedGenres = Set.<String>of();
		final var expectedCastMembers = Set.<String>of();
		final Resource expectedVideo = null;
		final Resource expectedTrailer = null;
		final Resource expectedBanner = null;
		final Resource expectedThumb = null;
		final Resource expectedThumbHalf = null;

		final var expectedErrorCount = 1;
		final var expectedErrorMessage = "Rating cannot be null";

		final var video = Fixture.Videos.video();

		final var command = new UpdateVideoCommand(
		  video.getId().toString(),
		  expectedTitle,
		  expectedDescription,
		  expectedLaunchedAt,
		  expectedDuration,
		  expectedOpened,
		  expectedPublished,
		  expectedRating,
		  expectedCategories,
		  expectedGenres,
		  expectedCastMembers,
		  expectedVideo,
		  expectedTrailer,
		  expectedBanner,
		  expectedThumb,
		  expectedThumbHalf
		);

		when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(video)));
		final var error = assertThrows(NotificationException.class, () -> useCase.execute(command));

		assertNotNull(error);
		assertEquals(expectedErrorCount, error.getErrors().size());
		assertEquals(expectedErrorMessage, error.getErrors().get(0).message());

		verify(videoGateway, times(0)).update(any());
	}

	@Test
	public void givenNullLaunchYear_whenCallsUpdateVideo_shouldReturnDomainException() {
		final var expectedTitle = Fixture.title();
		final var expectedDescription = Fixture.description();
		final Integer expectedLaunchedAt = null;
		final var expectedDuration = Fixture.duration();
		final var expectedOpened = Fixture.bool();
		final var expectedPublished = Fixture.bool();
		final var expectedRating = Fixture.Videos.rating();
		final var expectedCategories = Set.<String>of();
		final var expectedGenres = Set.<String>of();
		final var expectedCastMembers = Set.<String>of();
		final Resource expectedVideo = null;
		final Resource expectedTrailer = null;
		final Resource expectedBanner = null;
		final Resource expectedThumb = null;
		final Resource expectedThumbHalf = null;

		final var expectedErrorCount = 1;
		final var expectedErrorMessage = "LaunchedAt cannot be null";

		final var video = Fixture.Videos.video();

		final var command = new UpdateVideoCommand(
		  video.getId().toString(),
		  expectedTitle,
		  expectedDescription,
		  expectedLaunchedAt,
		  expectedDuration,
		  expectedOpened,
		  expectedPublished,
		  expectedRating.getName(),
		  expectedCategories,
		  expectedGenres,
		  expectedCastMembers,
		  expectedVideo,
		  expectedTrailer,
		  expectedBanner,
		  expectedThumb,
		  expectedThumbHalf
		);

		when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(video)));
		final var error = assertThrows(NotificationException.class, () -> useCase.execute(command));

		assertNotNull(error);
		assertEquals(expectedErrorCount, error.getErrors().size());
		assertEquals(expectedErrorMessage, error.getErrors().get(0).message());

		verify(videoGateway, times(0)).update(any());
	}

	@Test
	public void givenValidCommand_whenCallsUpdateVideoAndSomeCategoriesDoesNotExits_shouldReturnDomainException() {
		final var expectedTitle = Fixture.title();
		final var expectedDescription = Fixture.description();
		final var expectedLaunchedAt = Fixture.year();
		final var expectedDuration = Fixture.duration();
		final var expectedOpened = Fixture.bool();
		final var expectedPublished = Fixture.bool();
		final var expectedRating = Fixture.Videos.rating();
		final var expectedCategories = Set.of(Fixture.Categories.category().getId());
		final var expectedGenres = Set.of(Fixture.Genres.genre().getId());
		final var expectedCastMembers = Set.of(Fixture.CastMembers.castMember().getId());
		final Resource expectedVideo = null;
		final Resource expectedTrailer = null;
		final Resource expectedBanner = null;
		final Resource expectedThumb = null;
		final Resource expectedThumbHalf = null;

		final var expectedErrorCount = 1;
		final var expectedErrorMessage = "Some categories could not be found: %s".formatted(Fixture.Categories.category().getId().getValue());

		final var video = Fixture.Videos.video();

		final var command = new UpdateVideoCommand(
		  video.getId().toString(),
		  expectedTitle,
		  expectedDescription,
		  expectedLaunchedAt,
		  expectedDuration,
		  expectedOpened,
		  expectedPublished,
		  expectedRating.getName(),
		  asStrings(expectedCategories),
		  asStrings(expectedGenres),
		  asStrings(expectedCastMembers),
		  expectedVideo,
		  expectedTrailer,
		  expectedBanner,
		  expectedThumb,
		  expectedThumbHalf
		);

		when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(video)));
		when(categoryGateway.existsByIds(any())).thenReturn(new ArrayList<>());
		when(genreGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedGenres));
		when(castMemberGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCastMembers));

		final var error = assertThrows(NotificationException.class, () -> useCase.execute(command));

		assertNotNull(error);
		assertEquals(expectedErrorCount, error.getErrors().size());
		assertEquals(expectedErrorMessage, error.getErrors().get(0).message());

		verify(videoGateway, times(0)).update(any());
	}

	@Test
	public void givenValidCommand_whenCallsUpdateVideoAndSomeGenresDoesNotExits_shouldReturnDomainException() {
		final var expectedTitle = Fixture.title();
		final var expectedDescription = Fixture.description();
		final var expectedLaunchedAt = Fixture.year();
		final var expectedDuration = Fixture.duration();
		final var expectedOpened = Fixture.bool();
		final var expectedPublished = Fixture.bool();
		final var expectedRating = Fixture.Videos.rating();
		final var expectedCategories = Set.of(Fixture.Categories.category().getId());
		final var expectedGenres = Set.of(Fixture.Genres.genre().getId());
		final var expectedCastMembers = Set.of(Fixture.CastMembers.castMember().getId());
		final Resource expectedVideo = null;
		final Resource expectedTrailer = null;
		final Resource expectedBanner = null;
		final Resource expectedThumb = null;
		final Resource expectedThumbHalf = null;

		final var expectedErrorCount = 1;
		final var expectedErrorMessage = "Some genres could not be found: %s".formatted(Fixture.Genres.genre().getId().getValue());

		final var video = Fixture.Videos.video();

		final var command = new UpdateVideoCommand(
		  video.getId().toString(),
		  expectedTitle,
		  expectedDescription,
		  expectedLaunchedAt,
		  expectedDuration,
		  expectedOpened,
		  expectedPublished,
		  expectedRating.getName(),
		  asStrings(expectedCategories),
		  asStrings(expectedGenres),
		  asStrings(expectedCastMembers),
		  expectedVideo,
		  expectedTrailer,
		  expectedBanner,
		  expectedThumb,
		  expectedThumbHalf
		);

		when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(video)));
		when(categoryGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCategories));
		when(genreGateway.existsByIds(any())).thenReturn(new ArrayList<>());
		when(castMemberGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCastMembers));

		final var error = assertThrows(NotificationException.class, () -> useCase.execute(command));

		assertNotNull(error);
		assertEquals(expectedErrorCount, error.getErrors().size());
		assertEquals(expectedErrorMessage, error.getErrors().get(0).message());

		verify(videoGateway, times(0)).create(any());
	}

	@Test
	public void givenValidCommand_whenCallsUpdateVideoAndSomeMembersDoesNotExits_shouldReturnDomainException() {
		final var expectedTitle = Fixture.title();
		final var expectedDescription = Fixture.description();
		final var expectedLaunchedAt = Fixture.year();
		final var expectedDuration = Fixture.duration();
		final var expectedOpened = Fixture.bool();
		final var expectedPublished = Fixture.bool();
		final var expectedRating = Fixture.Videos.rating();
		final var expectedCategories = Set.of(Fixture.Categories.category().getId());
		final var expectedGenres = Set.of(Fixture.Genres.genre().getId());
		final var expectedCastMembers = Set.of(Fixture.CastMembers.castMember().getId());
		final Resource expectedVideo = null;
		final Resource expectedTrailer = null;
		final Resource expectedBanner = null;
		final Resource expectedThumb = null;
		final Resource expectedThumbHalf = null;

		final var expectedErrorCount = 1;
		final var expectedErrorMessage = "Some cast members could not be found: %s".formatted(Fixture.CastMembers.castMember().getId().getValue());

		final var video = Fixture.Videos.video();

		final var command = new UpdateVideoCommand(
		  video.getId().toString(),
		  expectedTitle,
		  expectedDescription,
		  expectedLaunchedAt,
		  expectedDuration,
		  expectedOpened,
		  expectedPublished,
		  expectedRating.getName(),
		  asStrings(expectedCategories),
		  asStrings(expectedGenres),
		  asStrings(expectedCastMembers),
		  expectedVideo,
		  expectedTrailer,
		  expectedBanner,
		  expectedThumb,
		  expectedThumbHalf
		);

		when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(video)));
		when(categoryGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCategories));
		when(genreGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedGenres));
		when(castMemberGateway.existsByIds(any())).thenReturn(new ArrayList<>());

		final var error = assertThrows(NotificationException.class, () -> useCase.execute(command));

		assertNotNull(error);
		assertEquals(expectedErrorCount, error.getErrors().size());
		assertEquals(expectedErrorMessage, error.getErrors().get(0).message());

		verify(videoGateway, times(0)).update(any());
	}

	@Test
	public void givenValidCommand_whenCallsUpdateVideoThrowsException_shouldCallClearResources() {
		final var expectedTitle = Fixture.title();
		final var expectedDescription = Fixture.description();
		final var expectedLaunchedAt = Fixture.year();
		final var expectedDuration = Fixture.duration();
		final var expectedOpened = Fixture.bool();
		final var expectedPublished = Fixture.bool();
		final var expectedRating = Fixture.Videos.rating();
		final var expectedCategories = Set.of(Fixture.Categories.category().getId());
		final var expectedGenres = Set.of(Fixture.Genres.genre().getId());
		final var expectedCastMembers = Set.of(Fixture.CastMembers.castMember().getId());
		final Resource expectedVideo = Fixture.Videos.resource(Resource.Type.VIDEO);
		final Resource expectedTrailer = Fixture.Videos.resource(Resource.Type.TRAILER);
		final Resource expectedBanner = Fixture.Videos.resource(Resource.Type.BANNER);
		final Resource expectedThumb = Fixture.Videos.resource(Resource.Type.THUMBNAIL);
		final Resource expectedThumbHalf = Fixture.Videos.resource(Resource.Type.THUMBNAIL_HALF);

		final var expectedErrorMessage = "Error on create video was observed";

		final var video = Fixture.Videos.video();

		final var command = new UpdateVideoCommand(
		  video.getId().toString(),
		  expectedTitle,
		  expectedDescription,
		  expectedLaunchedAt,
		  expectedDuration,
		  expectedOpened,
		  expectedPublished,
		  expectedRating.getName(),
		  asStrings(expectedCategories),
		  asStrings(expectedGenres),
		  asStrings(expectedCastMembers),
		  expectedVideo,
		  expectedTrailer,
		  expectedBanner,
		  expectedThumb,
		  expectedThumbHalf
		);

		when(videoGateway.findById(any())).thenReturn(Optional.of(Video.with(video)));
		when(categoryGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCategories));
		when(genreGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedGenres));
		when(castMemberGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCastMembers));
		when(videoGateway.update(any())).thenThrow(new RuntimeException("Internal server error"));

		mockImageMedia();
		mockAudioVideoMedia();

		final var error = assertThrows(InternalException.class, () -> useCase.execute(command));

		assertNotNull(error);
		assertTrue(error.getMessage().startsWith(expectedErrorMessage));

	}

	private void mockImageMedia() {
		when(mediaResourceGateway.storeImage(any(), any()))
		  .thenAnswer(t -> {
			  final var resource = t.getArgument(1, Resource.class);
			  return ImageMedia.with(UUID.randomUUID().toString(), resource.name(), "/img");
		  });
	}

	private void mockAudioVideoMedia() {
		when(mediaResourceGateway.storeAudioVideo(any(), any()))
		  .thenAnswer(t -> {
			  final var resource = t.getArgument(1, Resource.class);
			  return AudioVideoMedia.with(UUID.randomUUID().toString(), resource.name(), "/img", "", MediaStatus.PENDING);
		  });
	}
}

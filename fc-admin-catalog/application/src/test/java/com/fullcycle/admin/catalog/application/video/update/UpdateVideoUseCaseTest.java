package com.fullcycle.admin.catalog.application.video.update;

import com.fullcycle.admin.catalog.application.Fixture;
import com.fullcycle.admin.catalog.application.UseCaseTest;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalog.domain.category.CategoryGateway;
import com.fullcycle.admin.catalog.domain.genre.GenreGateway;
import com.fullcycle.admin.catalog.domain.video.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
		  castMemberGateway
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
		  video.getId().getValue().toString(),
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

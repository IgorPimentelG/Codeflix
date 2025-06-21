package com.fullcycle.admin.catalog.application.video.create;

import com.fullcycle.admin.catalog.application.Fixture;
import com.fullcycle.admin.catalog.application.UseCaseTest;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalog.domain.category.CategoryGateway;
import com.fullcycle.admin.catalog.domain.errors.InternalException;
import com.fullcycle.admin.catalog.domain.errors.NotificationException;
import com.fullcycle.admin.catalog.domain.genre.GenreGateway;
import com.fullcycle.admin.catalog.domain.resource.Resource;
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

public class CreateVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateVideoUseCase useCase;

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
    public void givenValidCommand_whenCallsCreateVideo_shouldReturnVideoId() {
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
        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumb = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var command = new CreateVideoCommand(
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

        when(categoryGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCategories));
        when(genreGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedGenres));
        when(castMemberGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCastMembers));
        when(videoGateway.create(any())).thenAnswer(returnsFirstArg());

        mockImageMedia();
        mockAudioVideoMedia();

        final var output = useCase.execute(command);

        assertNotNull(output);
        assertNotNull(output.id());

        verify(videoGateway).create(argThat(video ->
            Objects.equals(expectedTitle, video.getTitle()) &&
            Objects.equals(expectedDescription, video.getDescription()) &&
            Objects.equals(expectedLaunchedAt, video.getLaunchedAt().getValue()) &&
            Objects.equals(expectedDuration, video.getDuration()) &&
            Objects.equals(expectedOpened, video.isOpened()) &&
            Objects.equals(expectedPublished, video.isPublished()) &&
            Objects.equals(expectedRating, video.getRating()) &&
            Objects.equals(expectedCategories, video.getCategories()) &&
            Objects.equals(expectedGenres, video.getGenres()) &&
            Objects.equals(expectedCastMembers, video.getCastMembers()) &&
            video.getVideo().isPresent() &&
            video.getBanner().isPresent() &&
            video.getTrailer().isPresent() &&
            video.getThumbnail().isPresent() &&
            video.getThumbnailHalf().isPresent()
        ));
    }

    @Test
    public void givenValidCommandWithoutRelations_whenCallsCreateVideo_shouldReturnVideoId() {
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.description();
        final var expectedLaunchedAt = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<String>of();
        final var expectedGenres = Set.<String>of();
        final var expectedCastMembers = Set.<String>of();
        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumb = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var command = new CreateVideoCommand(
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

        when(videoGateway.create(any())).thenAnswer(returnsFirstArg());

        mockImageMedia();
        mockAudioVideoMedia();

        final var output = useCase.execute(command);

        assertNotNull(output);
        assertNotNull(output.id());

        verify(videoGateway).create(argThat(video ->
          Objects.equals(expectedTitle, video.getTitle()) &&
            Objects.equals(expectedDescription, video.getDescription()) &&
            Objects.equals(expectedLaunchedAt, video.getLaunchedAt().getValue()) &&
            Objects.equals(expectedDuration, video.getDuration()) &&
            Objects.equals(expectedOpened, video.isOpened()) &&
            Objects.equals(expectedPublished, video.isPublished()) &&
            Objects.equals(expectedRating, video.getRating()) &&
            Objects.equals(expectedCategories, video.getCategories()) &&
            Objects.equals(expectedGenres, video.getGenres()) &&
            Objects.equals(expectedCastMembers, video.getCastMembers()) &&
            video.getVideo().isPresent() &&
            video.getBanner().isPresent() &&
            video.getTrailer().isPresent() &&
            video.getThumbnail().isPresent() &&
            video.getThumbnailHalf().isPresent()
        ));
    }

    @Test
    public void givenValidCommandWithoutResources_whenCallsCreateVideo_shouldReturnVideoId() {
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

        final var command = new CreateVideoCommand(
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

        when(categoryGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCategories));
        when(genreGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedGenres));
        when(castMemberGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCastMembers));
        when(videoGateway.create(any())).thenAnswer(returnsFirstArg());

        final var output = useCase.execute(command);

        assertNotNull(output);
        assertNotNull(output.id());

        verify(videoGateway).create(argThat(video ->
          Objects.equals(expectedTitle, video.getTitle()) &&
            Objects.equals(expectedDescription, video.getDescription()) &&
            Objects.equals(expectedLaunchedAt, video.getLaunchedAt().getValue()) &&
            Objects.equals(expectedDuration, video.getDuration()) &&
            Objects.equals(expectedOpened, video.isOpened()) &&
            Objects.equals(expectedPublished, video.isPublished()) &&
            Objects.equals(expectedRating, video.getRating()) &&
            Objects.equals(expectedCategories, video.getCategories()) &&
            Objects.equals(expectedGenres, video.getGenres()) &&
            Objects.equals(expectedCastMembers, video.getCastMembers()) &&
            video.getVideo().isEmpty() &&
            video.getBanner().isEmpty() &&
            video.getTrailer().isEmpty() &&
            video.getThumbnail().isEmpty() &&
            video.getThumbnailHalf().isEmpty()
        ));
    }

    @Test
    public void givenNullTitle_whenCallsCreateVideo_shouldReturnDomainException() {
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

        final var command = new CreateVideoCommand(
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

        final var error = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertNotNull(error);
        assertEquals(expectedErrorCount, error.getErrors().size());
        assertEquals(expectedErrorMessage, error.getErrors().get(0).message());

        verify(videoGateway, times(0)).create(any());
    }

    @Test
    public void givenEmptyTitle_whenCallsCreateVideo_shouldReturnDomainException() {
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

        final var command = new CreateVideoCommand(
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

        final var error = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertNotNull(error);
        assertEquals(expectedErrorCount, error.getErrors().size());
        assertEquals(expectedErrorMessage, error.getErrors().get(0).message());

        verify(videoGateway, times(0)).create(any());
    }

    @Test
    public void givenNullRating_whenCallsCreateVideo_shouldReturnDomainException() {
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

        final var command = new CreateVideoCommand(
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

        final var error = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertNotNull(error);
        assertEquals(expectedErrorCount, error.getErrors().size());
        assertEquals(expectedErrorMessage, error.getErrors().get(0).message());

        verify(videoGateway, times(0)).create(any());
    }

    @Test
    public void givenInvalidRating_whenCallsCreateVideo_shouldReturnDomainException() {
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

        final var command = new CreateVideoCommand(
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

        final var error = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertNotNull(error);
        assertEquals(expectedErrorCount, error.getErrors().size());
        assertEquals(expectedErrorMessage, error.getErrors().get(0).message());

        verify(videoGateway, times(0)).create(any());
    }

    @Test
    public void givenNullLaunchYear_whenCallsCreateVideo_shouldReturnDomainException() {
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

        final var command = new CreateVideoCommand(
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

        final var error = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertNotNull(error);
        assertEquals(expectedErrorCount, error.getErrors().size());
        assertEquals(expectedErrorMessage, error.getErrors().get(0).message());

        verify(videoGateway, times(0)).create(any());
    }

    @Test
    public void givenValidCommand_whenCallsCreateVideoAndSomeCategoriesDoesNotExits_shouldReturnDomainException() {
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

        final var command = new CreateVideoCommand(
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

        when(categoryGateway.existsByIds(any())).thenReturn(new ArrayList<>());
        when(genreGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedGenres));
        when(castMemberGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCastMembers));

        final var error = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertNotNull(error);
        assertEquals(expectedErrorCount, error.getErrors().size());
        assertEquals(expectedErrorMessage, error.getErrors().get(0).message());

        verify(videoGateway, times(0)).create(any());
    }

    @Test
    public void givenValidCommand_whenCallsCreateVideoAndSomeGenresDoesNotExits_shouldReturnDomainException() {
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

        final var command = new CreateVideoCommand(
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
    public void givenValidCommand_whenCallsCreateVideoAndSomeMembersDoesNotExits_shouldReturnDomainException() {
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

        final var command = new CreateVideoCommand(
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

        when(categoryGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCategories));
        when(genreGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedGenres));
        when(castMemberGateway.existsByIds(any())).thenReturn(new ArrayList<>());

        final var error = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertNotNull(error);
        assertEquals(expectedErrorCount, error.getErrors().size());
        assertEquals(expectedErrorMessage, error.getErrors().get(0).message());

        verify(videoGateway, times(0)).create(any());
    }

    @Test
    public void givenValidCommand_whenCallsCreateVideoThrowsException_shouldCallClearResources() {
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
        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumb = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var expectedErrorMessage = "Error on create video was observed";


        final var command = new CreateVideoCommand(
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

        when(categoryGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCategories));
        when(genreGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedGenres));
        when(castMemberGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCastMembers));
        when(videoGateway.create(any())).thenThrow(new RuntimeException("Internal server error"));

        mockImageMedia();
        mockAudioVideoMedia();

        final var error = assertThrows(InternalException.class, () -> useCase.execute(command));

        assertNotNull(error);
        assertTrue(error.getMessage().startsWith(expectedErrorMessage));

        verify(mediaResourceGateway).clearResources(any());
    }

    private void mockImageMedia() {
        when(mediaResourceGateway.storeImage(any(), any()))
            .thenAnswer(t -> {
                final var resource = t.getArgument(1, VideoResource.class);
                final var name = resource.resource().name();
                return ImageMedia.with(UUID.randomUUID().toString(), name, "/location");
            });
    }

    private void mockAudioVideoMedia() {
        when(mediaResourceGateway.storeAudioVideo(any(), any()))
          .thenAnswer(t -> {
              final var resource = t.getArgument(1, VideoResource.class);
              final var name = resource.resource().name();
              return AudioVideoMedia.with(UUID.randomUUID().toString(), name, "/raw", "/encoded", MediaStatus.PENDING);
          });
    }
}

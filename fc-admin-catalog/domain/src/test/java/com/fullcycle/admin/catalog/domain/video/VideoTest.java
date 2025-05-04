package com.fullcycle.admin.catalog.domain.video;

import com.fullcycle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.genre.GenreID;
import com.fullcycle.admin.catalog.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class VideoTest {

    @Test
    public void givenValidParams_whenCallsNewVideo_shouldInstantiate() {
        final var expectedTitle = "any title";
        final var expectedDescription = "any description";
        final var expectedLaunchedAt = Year.now();
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMember = Set.of(CastMemberID.unique());

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
            expectedCastMember
        );

        assertNotNull(video);
        assertNotNull(video.getId());
        assertEquals(expectedTitle, video.getTitle());
        assertEquals(expectedDescription, video.getDescription());
        assertEquals(expectedLaunchedAt, video.getLaunchedAt());
        assertEquals(expectedDuration, video.getDuration());
        assertEquals(expectedOpened, video.isOpened());
        assertEquals(expectedPublished, video.isPublished());
        assertEquals(expectedRating, video.getRating());
        assertEquals(expectedCategories, video.getCategories());
        assertEquals(expectedGenres, video.getGenres());
        assertEquals(expectedCastMember, video.getCastMembers());
        assertTrue(video.getVideo().isEmpty());
        assertTrue(video.getTrailer().isEmpty());
        assertTrue(video.getBanner().isEmpty());
        assertTrue(video.getThumbnail().isEmpty());
        assertTrue(video.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> video.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidVideo_whenCallsUpdate_shouldReturnUpdated() {
        final var expectedTitle = "any title updated";
        final var expectedDescription = "any description updated";
        final var expectedLaunchedAt = Year.now();
        final var expectedDuration = 100.00;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMember = Set.of(CastMemberID.unique());

        final var video = Video.newVideo(
          "any title",
          "any description",
          Year.now(),
          200.00,
          Rating.AGE_10,
          true,
          true,
          Collections.emptySet(),
          Collections.emptySet(),
          Collections.emptySet()
        );

        final var videoUpdated = Video.with(video).update(
          expectedTitle,
          expectedDescription,
          expectedLaunchedAt,
          expectedDuration,
          expectedRating,
          expectedOpened,
          expectedPublished,
          expectedCategories,
          expectedGenres,
          expectedCastMember
        );

        assertNotNull(videoUpdated);
        assertNotNull(videoUpdated.getId());
        assertEquals(expectedTitle, videoUpdated.getTitle());
        assertEquals(expectedDescription, videoUpdated.getDescription());
        assertEquals(expectedLaunchedAt, videoUpdated.getLaunchedAt());
        assertEquals(expectedDuration, videoUpdated.getDuration());
        assertEquals(expectedOpened, videoUpdated.isOpened());
        assertEquals(expectedPublished, videoUpdated.isPublished());
        assertEquals(expectedRating, videoUpdated.getRating());
        assertEquals(expectedCategories, videoUpdated.getCategories());
        assertEquals(expectedGenres, videoUpdated.getGenres());
        assertEquals(expectedCastMember, videoUpdated.getCastMembers());
        assertNotNull(videoUpdated.getCreatedAt());
        assertNotNull(videoUpdated.getUpdatedAt());
        assertTrue(videoUpdated.getVideo().isEmpty());
        assertTrue(videoUpdated.getTrailer().isEmpty());
        assertTrue(videoUpdated.getBanner().isEmpty());
        assertTrue(videoUpdated.getThumbnail().isEmpty());
        assertTrue(videoUpdated.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> video.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidVideo_whenCallsSetVideo_shouldReturnUpdated() {
        final var expectedTitle = "any title";
        final var expectedDescription = "any description";
        final var expectedLaunchedAt = Year.now();
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMember = Set.of(CastMemberID.unique());

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
          expectedCastMember
        );

        final var videoMedia = AudioVideoMedia.with("any", "any name", "video.mp4", "/video", MediaStatus.PENDING);
        final var videoUpdated = Video.with(video).setVideo(videoMedia);

        assertNotNull(videoUpdated);
        assertNotNull(videoUpdated.getId());
        assertEquals(expectedTitle, videoUpdated.getTitle());
        assertEquals(expectedDescription, videoUpdated.getDescription());
        assertEquals(expectedLaunchedAt, videoUpdated.getLaunchedAt());
        assertEquals(expectedDuration, videoUpdated.getDuration());
        assertEquals(expectedOpened, videoUpdated.isOpened());
        assertEquals(expectedPublished, videoUpdated.isPublished());
        assertEquals(expectedRating, videoUpdated.getRating());
        assertEquals(expectedCategories, videoUpdated.getCategories());
        assertEquals(expectedGenres, videoUpdated.getGenres());
        assertEquals(expectedCastMember, videoUpdated.getCastMembers());
        assertNotNull(videoUpdated.getCreatedAt());
        assertNotNull(videoUpdated.getUpdatedAt());
        assertFalse(videoUpdated.getVideo().isEmpty());
        assertTrue(videoUpdated.getTrailer().isEmpty());
        assertTrue(videoUpdated.getBanner().isEmpty());
        assertTrue(videoUpdated.getThumbnail().isEmpty());
        assertTrue(videoUpdated.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> video.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidVideo_whenCallsSetTrailer_shouldReturnUpdated() {
        final var expectedTitle = "any title";
        final var expectedDescription = "any description";
        final var expectedLaunchedAt = Year.now();
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMember = Set.of(CastMemberID.unique());

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
          expectedCastMember
        );

        final var videoMedia = AudioVideoMedia.with("any", "any name", "video.mp4", "/video", MediaStatus.PENDING);
        final var videoUpdated = Video.with(video).setTrailer(videoMedia);

        assertNotNull(videoUpdated);
        assertNotNull(videoUpdated.getId());
        assertEquals(expectedTitle, videoUpdated.getTitle());
        assertEquals(expectedDescription, videoUpdated.getDescription());
        assertEquals(expectedLaunchedAt, videoUpdated.getLaunchedAt());
        assertEquals(expectedDuration, videoUpdated.getDuration());
        assertEquals(expectedOpened, videoUpdated.isOpened());
        assertEquals(expectedPublished, videoUpdated.isPublished());
        assertEquals(expectedRating, videoUpdated.getRating());
        assertEquals(expectedCategories, videoUpdated.getCategories());
        assertEquals(expectedGenres, videoUpdated.getGenres());
        assertEquals(expectedCastMember, videoUpdated.getCastMembers());
        assertNotNull(videoUpdated.getCreatedAt());
        assertNotNull(videoUpdated.getUpdatedAt());
        assertTrue(videoUpdated.getVideo().isEmpty());
        assertFalse(videoUpdated.getTrailer().isEmpty());
        assertTrue(videoUpdated.getBanner().isEmpty());
        assertTrue(videoUpdated.getThumbnail().isEmpty());
        assertTrue(videoUpdated.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> video.validate(new ThrowsValidationHandler()));
    }


    @Test
    public void givenValidVideo_whenCallsSetBanner_shouldReturnUpdated() {
        final var expectedTitle = "any title";
        final var expectedDescription = "any description";
        final var expectedLaunchedAt = Year.now();
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMember = Set.of(CastMemberID.unique());

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
          expectedCastMember
        );

        final var imageMedia = ImageMedia.with("any", "any name", "/image");
        final var videoUpdated = Video.with(video).setBanner(imageMedia);

        assertNotNull(videoUpdated);
        assertNotNull(videoUpdated.getId());
        assertEquals(expectedTitle, videoUpdated.getTitle());
        assertEquals(expectedDescription, videoUpdated.getDescription());
        assertEquals(expectedLaunchedAt, videoUpdated.getLaunchedAt());
        assertEquals(expectedDuration, videoUpdated.getDuration());
        assertEquals(expectedOpened, videoUpdated.isOpened());
        assertEquals(expectedPublished, videoUpdated.isPublished());
        assertEquals(expectedRating, videoUpdated.getRating());
        assertEquals(expectedCategories, videoUpdated.getCategories());
        assertEquals(expectedGenres, videoUpdated.getGenres());
        assertEquals(expectedCastMember, videoUpdated.getCastMembers());
        assertNotNull(videoUpdated.getCreatedAt());
        assertNotNull(videoUpdated.getUpdatedAt());
        assertTrue(videoUpdated.getVideo().isEmpty());
        assertTrue(videoUpdated.getTrailer().isEmpty());
        assertFalse(videoUpdated.getBanner().isEmpty());
        assertTrue(videoUpdated.getThumbnail().isEmpty());
        assertTrue(videoUpdated.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> video.validate(new ThrowsValidationHandler()));
    }


    @Test
    public void givenValidVideo_whenCallsSetThumbnail_shouldReturnUpdated() {
        final var expectedTitle = "any title";
        final var expectedDescription = "any description";
        final var expectedLaunchedAt = Year.now();
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMember = Set.of(CastMemberID.unique());

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
          expectedCastMember
        );

        final var imageMedia = ImageMedia.with("any", "any name", "/image");
        final var videoUpdated = Video.with(video).setThumbnail(imageMedia);

        assertNotNull(videoUpdated);
        assertNotNull(videoUpdated.getId());
        assertEquals(expectedTitle, videoUpdated.getTitle());
        assertEquals(expectedDescription, videoUpdated.getDescription());
        assertEquals(expectedLaunchedAt, videoUpdated.getLaunchedAt());
        assertEquals(expectedDuration, videoUpdated.getDuration());
        assertEquals(expectedOpened, videoUpdated.isOpened());
        assertEquals(expectedPublished, videoUpdated.isPublished());
        assertEquals(expectedRating, videoUpdated.getRating());
        assertEquals(expectedCategories, videoUpdated.getCategories());
        assertEquals(expectedGenres, videoUpdated.getGenres());
        assertEquals(expectedCastMember, videoUpdated.getCastMembers());
        assertNotNull(videoUpdated.getCreatedAt());
        assertNotNull(videoUpdated.getUpdatedAt());
        assertTrue(videoUpdated.getVideo().isEmpty());
        assertTrue(videoUpdated.getTrailer().isEmpty());
        assertTrue(videoUpdated.getBanner().isEmpty());
        assertFalse(videoUpdated.getThumbnail().isEmpty());
        assertTrue(videoUpdated.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> video.validate(new ThrowsValidationHandler()));
    }


    @Test
    public void givenValidVideo_whenCallsSetThumbnailHalf_shouldReturnUpdated() {
        final var expectedTitle = "any title";
        final var expectedDescription = "any description";
        final var expectedLaunchedAt = Year.now();
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMember = Set.of(CastMemberID.unique());

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
          expectedCastMember
        );

        final var imageMedia = ImageMedia.with("any", "any name", "/image");
        final var videoUpdated = Video.with(video).setThumbnailHalf(imageMedia);

        assertNotNull(videoUpdated);
        assertNotNull(videoUpdated.getId());
        assertEquals(expectedTitle, videoUpdated.getTitle());
        assertEquals(expectedDescription, videoUpdated.getDescription());
        assertEquals(expectedLaunchedAt, videoUpdated.getLaunchedAt());
        assertEquals(expectedDuration, videoUpdated.getDuration());
        assertEquals(expectedOpened, videoUpdated.isOpened());
        assertEquals(expectedPublished, videoUpdated.isPublished());
        assertEquals(expectedRating, videoUpdated.getRating());
        assertEquals(expectedCategories, videoUpdated.getCategories());
        assertEquals(expectedGenres, videoUpdated.getGenres());
        assertEquals(expectedCastMember, videoUpdated.getCastMembers());
        assertNotNull(videoUpdated.getCreatedAt());
        assertNotNull(videoUpdated.getUpdatedAt());
        assertTrue(videoUpdated.getVideo().isEmpty());
        assertTrue(videoUpdated.getTrailer().isEmpty());
        assertTrue(videoUpdated.getBanner().isEmpty());
        assertTrue(videoUpdated.getThumbnail().isEmpty());
        assertFalse(videoUpdated.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> video.validate(new ThrowsValidationHandler()));
    }


}



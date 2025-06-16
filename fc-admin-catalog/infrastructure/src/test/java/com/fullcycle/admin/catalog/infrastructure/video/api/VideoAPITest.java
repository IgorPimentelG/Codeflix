package com.fullcycle.admin.catalog.infrastructure.video.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcycle.admin.catalog.ControllerTest;
import com.fullcycle.admin.catalog.Fixture;
import com.fullcycle.admin.catalog.application.video.create.CreateVideoCommand;
import com.fullcycle.admin.catalog.application.video.create.CreateVideoOutput;
import com.fullcycle.admin.catalog.application.video.create.CreateVideoUseCase;
import com.fullcycle.admin.catalog.application.video.delete.DeleteVideoUseCase;
import com.fullcycle.admin.catalog.application.video.media.get.GetMediaUseCase;
import com.fullcycle.admin.catalog.application.video.media.get.MediaOutput;
import com.fullcycle.admin.catalog.application.video.media.upload.UploadMediaCommand;
import com.fullcycle.admin.catalog.application.video.media.upload.UploadMediaOutput;
import com.fullcycle.admin.catalog.application.video.media.upload.UploadMediaUseCase;
import com.fullcycle.admin.catalog.application.video.retrieve.get.GetVideoByIdUseCase;
import com.fullcycle.admin.catalog.application.video.retrieve.get.VideoOutput;
import com.fullcycle.admin.catalog.application.video.retrieve.list.ListVideosUseCase;
import com.fullcycle.admin.catalog.application.video.retrieve.list.VideoListOutput;
import com.fullcycle.admin.catalog.application.video.update.UpdateVideoCommand;
import com.fullcycle.admin.catalog.application.video.update.UpdateVideoOutput;
import com.fullcycle.admin.catalog.application.video.update.UpdateVideoUseCase;
import com.fullcycle.admin.catalog.domain.Identifier;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.errors.NotificationException;
import com.fullcycle.admin.catalog.domain.genre.GenreID;
import com.fullcycle.admin.catalog.domain.pagination.Pagination;
import com.fullcycle.admin.catalog.domain.pagination.VideoSearchQuery;
import com.fullcycle.admin.catalog.domain.validation.Error;
import com.fullcycle.admin.catalog.domain.video.Video;
import com.fullcycle.admin.catalog.domain.video.VideoID;
import com.fullcycle.admin.catalog.domain.video.VideoMediaType;
import com.fullcycle.admin.catalog.domain.video.VideoPreview;
import com.fullcycle.admin.catalog.infrastructure.api.VideoAPI;
import com.fullcycle.admin.catalog.infrastructure.video.models.CreateVideoRequest;
import com.fullcycle.admin.catalog.infrastructure.video.models.UpdateVideoRequest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.Year;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = VideoAPI.class)
public class VideoAPITest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper mapper;

	@MockitoBean
	private CreateVideoUseCase createVideoUseCase;

	@MockitoBean
	private GetVideoByIdUseCase getVideoByIdUseCase;

	@MockitoBean
	private UpdateVideoUseCase updateVideoUseCase;

	@MockitoBean
	private DeleteVideoUseCase deleteVideoUseCase;

	@MockitoBean
	private GetMediaUseCase getMediaUseCase;

	@MockitoBean
	private ListVideosUseCase listVideosUseCase;

	@MockitoBean
	private UploadMediaUseCase uploadMediaUseCase;

	@Test
	public void givenValidCommand_whenCallsCreateFull_shouldReturnAndId() throws Exception {
		final var video = Fixture.Videos.video();
		final var castMember = Fixture.CastMembers.castMember();
		final var genre = Fixture.Genres.genre();
		final var category = Fixture.Categories.category();

		final var expectedId = video.getId().getValue();
		final var expectedTitle = Fixture.title();
		final var expectedDescription = Fixture.description();
		final var expectedLaunchedAt = Fixture.year();
		final var expectedDuration = Fixture.duration();
		final var expectedOpened = Fixture.bool();
		final var expectedPublished = Fixture.bool();
		final var expectedRating = Fixture.Videos.rating();
		final var expectedCategories = Set.of(category.getId());
		final var expectedGenres = Set.of(genre.getId());
		final var expectedCastMembers = Set.of(castMember.getId());

		final var expectedVideo = new MockMultipartFile("video_file", "video.mp4", "video/mp4", "VIDEO".getBytes());
		final var expectedTrailer = new MockMultipartFile("trailer_file", "trailer.mp4", "trailer/mp4", "TRAILER".getBytes());
		final var expectedBanner = new MockMultipartFile("banner_file", "banner.mp4", "banner/mp4", "BANNER".getBytes());
		final var expectedThumb = new MockMultipartFile("thumb_file", "thumb.mp4", "thumb/mp4", "THUMB".getBytes());
		final var expectedThumbHalf = new MockMultipartFile("thumb_half_file", "thumb_half.mp4", "thumb_half/mp4", "THUMB_HALF".getBytes());

		when(createVideoUseCase.execute(any())).thenReturn(new CreateVideoOutput(expectedId.toString()));

		final var request = multipart("/videos")
		  .file(expectedVideo)
		  .file(expectedBanner)
		  .file(expectedTrailer)
		  .file(expectedThumb)
		  .file(expectedThumbHalf)
		  .param("title", expectedTitle)
		  .param("description", expectedDescription)
		  .param("year_launched", expectedLaunchedAt.toString())
		  .param("duration", expectedDuration.toString())
		  .param("opened", expectedOpened.toString())
		  .param("published", expectedPublished.toString())
		  .param("rating", expectedRating.toString())
		  .param("categories_id", category.getId().toString())
		  .param("genres_id", genre.getId().toString())
		  .param("cast_members_id", castMember.getId().toString())
		  .accept(MediaType.APPLICATION_JSON)
		  .contentType(MediaType.MULTIPART_FORM_DATA_VALUE);

		mvc.perform(request)
		  .andExpect(status().isCreated())
		  .andExpect(header().string("Location", "/videos/" + expectedId.toString()))
		  .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
		  .andExpect(jsonPath("$.id", equalTo(expectedId.toString())));

		final var captor = ArgumentCaptor.forClass(CreateVideoCommand.class);
		verify(createVideoUseCase).execute(captor.capture());
		final var command = captor.getValue();

		assertEquals(expectedTitle, command.title());
		assertEquals(expectedDescription, command.description());
		assertEquals(expectedLaunchedAt, command.launchedAt());
		assertEquals(expectedDuration, command.duration());
		assertEquals(expectedOpened, command.opened());
		assertEquals(expectedPublished, command.published());
		assertEquals(expectedRating.name(), command.rating());
		assertEquals(expectedCategories.stream().map(Identifier::toString).collect(Collectors.toSet()), command.categories());
		assertEquals(expectedGenres.stream().map(Identifier::toString).collect(Collectors.toSet()), command.genres());
		assertEquals(expectedCastMembers.stream().map(Identifier::toString).collect(Collectors.toSet()), command.members());
		assertEquals(expectedVideo.getOriginalFilename(), command.video().name());
		assertEquals(expectedTrailer.getOriginalFilename(), command.trailer().name());
		assertEquals(expectedBanner.getOriginalFilename(), command.banner().name());
		assertEquals(expectedThumb.getOriginalFilename(), command.thumbnail().name());
		assertEquals(expectedThumbHalf.getOriginalFilename(), command.thumbnailHalf().name());
	}

	@Test
	public void givenValidCommand_whenCallsCreatePartial_shouldReturnId() throws Exception {
		final var video = Fixture.Videos.video();
		final var castMember = Fixture.CastMembers.castMember();
		final var genre = Fixture.Genres.genre();
		final var category = Fixture.Categories.category();

		final var expectedId = video.getId().getValue();
		final var expectedTitle = Fixture.title();
		final var expectedDescription = Fixture.description();
		final var expectedLaunchedAt = Fixture.year();
		final var expectedDuration = Fixture.duration();
		final var expectedOpened = Fixture.bool();
		final var expectedPublished = Fixture.bool();
		final var expectedRating = Fixture.Videos.rating().name();
		final var expectedCategories = Set.of(category.getId().toString());
		final var expectedGenres = Set.of(genre.getId().toString());
		final var expectedCastMembers = Set.of(castMember.getId().toString());

		when(createVideoUseCase.execute(any())).thenReturn(new CreateVideoOutput(expectedId.toString()));

		final var payload = new CreateVideoRequest(
		  expectedTitle,
		  expectedDescription,
		  expectedLaunchedAt,
		  expectedDuration,
		  expectedOpened,
		  expectedPublished,
		  expectedRating,
		  expectedCategories,
		  expectedGenres,
		  expectedCastMembers
		);

		final var request = post("/videos")
		  .content(mapper.writeValueAsString(payload))
		  .accept(MediaType.APPLICATION_JSON)
		  .contentType(MediaType.APPLICATION_JSON);

		mvc.perform(request)
		  .andExpect(status().isCreated())
		  .andExpect(header().string("Location", "/videos/" + expectedId.toString()))
		  .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
		  .andExpect(jsonPath("$.id", equalTo(expectedId.toString())));

		final var captor = ArgumentCaptor.forClass(CreateVideoCommand.class);
		verify(createVideoUseCase).execute(captor.capture());
		final var command = captor.getValue();

		assertEquals(expectedTitle, command.title());
		assertEquals(expectedDescription, command.description());
		assertEquals(expectedLaunchedAt, command.launchedAt());
		assertEquals(expectedDuration, command.duration());
		assertEquals(expectedOpened, command.opened());
		assertEquals(expectedPublished, command.published());
		assertEquals(expectedRating, command.rating());
		assertEquals(expectedCategories, command.categories());
		assertEquals(expectedGenres, command.genres());
		assertEquals(expectedCastMembers, command.members());
		assertTrue(command.getVideo().isEmpty());
		assertTrue(command.getTrailer().isEmpty());
		assertTrue(command.getBanner().isEmpty());
		assertTrue(command.getThumbnail().isEmpty());
		assertTrue(command.getThumbnailHalf().isEmpty());
	}

	@Test
	public void givenValidId_whenCallsGetById_shouldReturnVideo() throws Exception {
		final var castMember = Fixture.CastMembers.castMember();
		final var genre = Fixture.Genres.genre();
		final var category = Fixture.Categories.category();

		final var expectedTitle = Fixture.title();
		final var expectedDescription = Fixture.description();
		final var expectedLaunchedAt = Fixture.year();
		final var expectedDuration = Fixture.duration();
		final var expectedOpened = Fixture.bool();
		final var expectedPublished = Fixture.bool();
		final var expectedRating = Fixture.Videos.rating();
		final var expectedCategories = Set.of(category.getId());
		final var expectedGenres = Set.of(genre.getId());
		final var expectedCastMembers = Set.of(castMember.getId());

		final var expectedVideo = Fixture.Videos.audioVideoMedia(VideoMediaType.VIDEO);
		final var expectedTrailer = Fixture.Videos.audioVideoMedia(VideoMediaType.TRAILER);
		final var expectedBanner = Fixture.Videos.imageMedia(VideoMediaType.BANNER);
		final var expectedThumb = Fixture.Videos.imageMedia(VideoMediaType.THUMBNAIL);
		final var expectedThumbHalf = Fixture.Videos.imageMedia(VideoMediaType.THUMBNAIL_HALF);

		final var video = Video.newVideo(
		  expectedTitle,
		  expectedDescription,
		  Year.of(expectedLaunchedAt),
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

		final var expectedId = video.getId().getValue();

		when(getVideoByIdUseCase.execute(any())).thenReturn(VideoOutput.from(video));

		final var request = get("/videos/{id}", expectedId.toString())
		  .accept(MediaType.APPLICATION_JSON);

		mvc.perform(request)
		  .andExpect(status().isOk())
		  .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
		  .andExpect(jsonPath("$.id", equalTo(expectedId.toString())))
		  .andExpect(jsonPath("$.title", equalTo(expectedTitle)))
		  .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
		  .andExpect(jsonPath("$.year_launched", equalTo(expectedLaunchedAt)))
		  .andExpect(jsonPath("$.duration", equalTo(expectedDuration)))
		  .andExpect(jsonPath("$.opened", equalTo(expectedOpened)))
		  .andExpect(jsonPath("$.published", equalTo(expectedPublished)))
		  .andExpect(jsonPath("$.rating", equalTo(expectedRating.name())))
		  .andExpect(jsonPath("$.created_at", notNullValue()))
		  .andExpect(jsonPath("$.updated_at", notNullValue()))
		  .andExpect(jsonPath("$.categories_id", hasSize(expectedCategories.size())))
		  .andExpect(jsonPath("$.genres_id", hasSize(expectedGenres.size())))
		  .andExpect(jsonPath("$.cast_members_id", hasSize(expectedCastMembers.size())))
		  .andExpect(jsonPath("$.video.checksum", equalTo(expectedVideo.getChecksum())))
		  .andExpect(jsonPath("$.trailer.checksum", equalTo(expectedTrailer.getChecksum())))
		  .andExpect(jsonPath("$.banner.checksum", equalTo(expectedBanner.getChecksum())))
		  .andExpect(jsonPath("$.thumbnail.checksum", equalTo(expectedThumb.getChecksum())))
		  .andExpect(jsonPath("$.thumbnail_half.checksum", equalTo(expectedThumbHalf.getChecksum())));
	}

	@Test
	public void givenValidCommand_whenCallsUpdateVideo_shouldReturnVideoId() throws Exception {
		final var video = Fixture.Videos.video();
		final var castMember = Fixture.CastMembers.castMember();
		final var genre = Fixture.Genres.genre();
		final var category = Fixture.Categories.category();

		final var expectedId = video.getId().getValue();
		final var expectedTitle = Fixture.title();
		final var expectedDescription = Fixture.description();
		final var expectedLaunchedAt = Fixture.year();
		final var expectedDuration = Fixture.duration();
		final var expectedOpened = Fixture.bool();
		final var expectedPublished = Fixture.bool();
		final var expectedRating = Fixture.Videos.rating().name();
		final var expectedCategories = Set.of(category.getId().toString());
		final var expectedGenres = Set.of(genre.getId().toString());
		final var expectedCastMembers = Set.of(castMember.getId().toString());

		when(updateVideoUseCase.execute(any())).thenReturn(new UpdateVideoOutput(expectedId));

		final var payload = new UpdateVideoRequest(
		  expectedTitle,
		  expectedDescription,
		  expectedLaunchedAt,
		  expectedDuration,
		  expectedOpened,
		  expectedPublished,
		  expectedRating,
		  expectedCategories,
		  expectedGenres,
		  expectedCastMembers
		);

		final var request = put("/videos/{id}", expectedId.toString())
		  .content(mapper.writeValueAsString(payload))
		  .accept(MediaType.APPLICATION_JSON)
		  .contentType(MediaType.APPLICATION_JSON);

		mvc.perform(request)
		  .andExpect(status().isOk())
		  .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
		  .andExpect(jsonPath("$.id", equalTo(expectedId.toString())));

		final var captor = ArgumentCaptor.forClass(UpdateVideoCommand.class);
		verify(updateVideoUseCase).execute(captor.capture());
		final var command = captor.getValue();

		assertEquals(expectedTitle, command.title());
		assertEquals(expectedDescription, command.description());
		assertEquals(expectedLaunchedAt, command.launchedAt());
		assertEquals(expectedDuration, command.duration());
		assertEquals(expectedOpened, command.opened());
		assertEquals(expectedPublished, command.published());
		assertEquals(expectedRating, command.rating());
		assertEquals(expectedCategories, command.categories());
		assertEquals(expectedGenres, command.genres());
		assertEquals(expectedCastMembers, command.members());
		assertTrue(command.getVideo().isEmpty());
		assertTrue(command.getTrailer().isEmpty());
		assertTrue(command.getBanner().isEmpty());
		assertTrue(command.getThumbnail().isEmpty());
		assertTrue(command.getThumbnailHalf().isEmpty());
	}

	@Test
	public void givenInvalidCommand_whenCallsUpdateVideo_shouldReturnNotification() throws Exception {
		final var video = Fixture.Videos.video();
		final var castMember = Fixture.CastMembers.castMember();
		final var genre = Fixture.Genres.genre();
		final var category = Fixture.Categories.category();

		final var expectedId = video.getId().getValue();
		final var expectedErrorMessage = "Title cannot be empty";
		final var expectedTitle = Fixture.title();
		final var expectedDescription = Fixture.description();
		final var expectedLaunchedAt = Fixture.year();
		final var expectedDuration = Fixture.duration();
		final var expectedOpened = Fixture.bool();
		final var expectedPublished = Fixture.bool();
		final var expectedRating = Fixture.Videos.rating().name();
		final var expectedCategories = Set.of(category.getId().toString());
		final var expectedGenres = Set.of(genre.getId().toString());
		final var expectedCastMembers = Set.of(castMember.getId().toString());

		when(updateVideoUseCase.execute(any())).thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

		final var payload = new UpdateVideoRequest(
		  expectedTitle,
		  expectedDescription,
		  expectedLaunchedAt,
		  expectedDuration,
		  expectedOpened,
		  expectedPublished,
		  expectedRating,
		  expectedCategories,
		  expectedGenres,
		  expectedCastMembers
		);

		final var request = put("/videos/{id}", expectedId.toString())
		  .content(mapper.writeValueAsString(payload))
		  .accept(MediaType.APPLICATION_JSON)
		  .contentType(MediaType.APPLICATION_JSON);

		mvc.perform(request)
		  .andExpect(status().isUnprocessableEntity())
		  .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
		  .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

		final var captor = ArgumentCaptor.forClass(UpdateVideoCommand.class);
		verify(updateVideoUseCase).execute(captor.capture());
		final var command = captor.getValue();

		assertEquals(expectedTitle, command.title());
		assertEquals(expectedDescription, command.description());
		assertEquals(expectedLaunchedAt, command.launchedAt());
		assertEquals(expectedDuration, command.duration());
		assertEquals(expectedOpened, command.opened());
		assertEquals(expectedPublished, command.published());
		assertEquals(expectedRating, command.rating());
		assertEquals(expectedCategories, command.categories());
		assertEquals(expectedGenres, command.genres());
		assertEquals(expectedCastMembers, command.members());
		assertTrue(command.getVideo().isEmpty());
		assertTrue(command.getTrailer().isEmpty());
		assertTrue(command.getBanner().isEmpty());
		assertTrue(command.getThumbnail().isEmpty());
		assertTrue(command.getThumbnailHalf().isEmpty());
	}

	@Test
	public void givenValidVideoIdAndFileType_whenCallsGetMediaById_shouldReturnContent() throws Exception {
		final var expectedVideo = Fixture.Videos.video();
		final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);
		final var expectedMediaOutput = new MediaOutput(
		  expectedResource.name(),
		  expectedResource.checksum(),
		  expectedResource.contentType(),
		  expectedResource.content()
		);

		when(getMediaUseCase.execute(any())).thenReturn(expectedMediaOutput);

		final var request = get("/videos/{id}/medias/{type}", expectedVideo.getId().toString(), VideoMediaType.VIDEO.name());

		mvc.perform(request)
		  .andExpect(status().isOk())
		  .andExpect(header().string("Content-Type", expectedResource.contentType()))
		  .andExpect(header().string("Content-Length", String.valueOf(expectedResource.content().length)))
		  .andExpect(header().string("Content-Disposition", "attachment; filename=\"%s\"".formatted(expectedResource.name())))
		  .andExpect(content().bytes(expectedResource.content()));
	}

	@Test
	public void givenValidId_whenCallsDeleteById_shouldDeleteVideo() throws Exception {
		final var expectedId = VideoID.unique();

		doNothing().when(deleteVideoUseCase).execute(any());

		final var request = delete("/videos/{id}", expectedId.getValue().toString());
		mvc.perform(request).andExpect(status().isNoContent());
		verify(deleteVideoUseCase).execute(eq(expectedId.getValue()));
	}

	@Test
	public void givenValidParams_whenCallsListVideos_shouldReturnPagination() throws Exception {
		final var video = VideoListOutput.from(new VideoPreview(UUID.randomUUID(), Fixture.title(), Fixture.description(), Instant.now(), Instant.now()));

		final var expectedPage = 0;
		final var expectedPerPage = 25;
		final var expectedTerms = "any";
		final var expectedSort = "title";
		final var expectedDirection = "asc";
		final var expectedCastMembers = UUID.randomUUID().toString();
		final var expectedCategories = UUID.randomUUID().toString();
		final var expectedGenres = UUID.randomUUID().toString();

		final var expectedItemsCount = 1;
		final var expectedTotal = 1;

		final var expectedItems = List.of(video);

		when(listVideosUseCase.execute(any())).thenReturn(
		  new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems)
		);

		final var request = get("/videos")
		  .param("page", String.valueOf(expectedPage))
		  .param("per_page", String.valueOf(expectedPerPage))
		  .param("search", expectedTerms)
		  .param("sort", expectedSort)
		  .param("direction", expectedDirection)
		  .param("cast_members_ids", expectedCastMembers)
		  .param("categories_ids", expectedCategories)
		  .param("genres_ids", expectedGenres)
		  .accept(MediaType.APPLICATION_JSON);

		mvc.perform(request)
		  .andExpect(status().isOk())
		  .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
		  .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
		  .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
		  .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
		  .andExpect(jsonPath("$.items[0].id", equalTo(video.id())))
		  .andExpect(jsonPath("$.items[0].title", equalTo(video.title())))
		  .andExpect(jsonPath("$.items[0].description", equalTo(video.description())))
		  .andExpect(jsonPath("$.items[0].created_at", equalTo(video.createdAt().toString())))
		  .andExpect(jsonPath("$.items[0].updated_at", equalTo(video.updatedAt().toString())));

		final var captor = ArgumentCaptor.forClass(VideoSearchQuery.class);
		verify(listVideosUseCase).execute(captor.capture());
		final var query = captor.getValue();

		assertEquals(expectedPage, query.page());
		assertEquals(expectedPerPage, query.perPage());
		assertEquals(expectedTerms, query.terms());
		assertEquals(expectedSort, query.sort());
		assertEquals(expectedDirection, query.direction());
		assertEquals(Set.of(CastMemberID.from(expectedCastMembers)), query.castMembers());
		assertEquals(Set.of(CategoryID.from(expectedCategories)), query.categories());
		assertEquals(Set.of(GenreID.from(expectedGenres)), query.genres());
	}

	@Test
	public void givenEmptyParams_whenCallsListVideos_shouldReturnPagination() throws Exception {
		final var video = VideoListOutput.from(new VideoPreview(UUID.randomUUID(), Fixture.title(), Fixture.description(), Instant.now(), Instant.now()));

		final var expectedPage = 0;
		final var expectedPerPage = 25;
		final var expectedTerms = "";
		final var expectedSort = "title";
		final var expectedDirection = "asc";
		final var expectedCastMembers = "";
		final var expectedCategories = "";
		final var expectedGenres = "";

		final var expectedItemsCount = 1;
		final var expectedTotal = 1;

		final var expectedItems = List.of(video);

		when(listVideosUseCase.execute(any())).thenReturn(
		  new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems)
		);

		final var request = get("/videos")
		  .accept(MediaType.APPLICATION_JSON);

		mvc.perform(request)
		  .andExpect(status().isOk())
		  .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
		  .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
		  .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
		  .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
		  .andExpect(jsonPath("$.items[0].id", equalTo(video.id())))
		  .andExpect(jsonPath("$.items[0].title", equalTo(video.title())))
		  .andExpect(jsonPath("$.items[0].description", equalTo(video.description())))
		  .andExpect(jsonPath("$.items[0].created_at", equalTo(video.createdAt().toString())))
		  .andExpect(jsonPath("$.items[0].updated_at", equalTo(video.updatedAt().toString())));

		final var captor = ArgumentCaptor.forClass(VideoSearchQuery.class);
		verify(listVideosUseCase).execute(captor.capture());
		final var query = captor.getValue();

		assertEquals(expectedPage, query.page());
		assertEquals(expectedPerPage, query.perPage());
		assertEquals(expectedTerms, query.terms());
		assertEquals(expectedSort, query.sort());
		assertEquals(expectedDirection, query.direction());
		assertTrue(query.castMembers().isEmpty());
		assertTrue(query.categories().isEmpty());
		assertTrue(query.genres().isEmpty());
	}

	@Test
	public void givenValidVideoIdAndFile_whenCallsUploadMedia_shouldStoreIt() throws Exception{
		final var expectedVideoId = UUID.randomUUID();
		final var expectedType = VideoMediaType.VIDEO;
		final var expectedResource = Fixture.Videos.resource(Fixture.Videos.mediaType());
		final var expectedVideo = new MockMultipartFile("media_file", expectedResource.name(), expectedResource.contentType(), expectedResource.content());

		when(uploadMediaUseCase.execute(any())).thenReturn(new UploadMediaOutput(expectedVideoId.toString(), expectedType));

		final var request = multipart("/videos/{id}/medias/{type}", expectedVideoId.toString(), expectedType.name())
		  .file(expectedVideo)
		  .accept(MediaType.APPLICATION_JSON)
		  .contentType(MediaType.MULTIPART_FORM_DATA_VALUE);

		mvc.perform(request)
		  .andExpect(status().isCreated())
		  .andExpect(jsonPath("$.video_id", equalTo(expectedVideoId.toString())))
		  .andExpect(jsonPath("$.media_type", equalTo(expectedType.name())));

		final var captor = ArgumentCaptor.forClass(UploadMediaCommand.class);
		verify(uploadMediaUseCase).execute(captor.capture());
		final var input = captor.getValue();

		assertEquals(expectedVideoId.toString(), input.videoId());
		assertEquals(expectedType, input.resource().type());
		assertEquals(expectedResource.checksum(), input.resource().resource().checksum());
	}
}


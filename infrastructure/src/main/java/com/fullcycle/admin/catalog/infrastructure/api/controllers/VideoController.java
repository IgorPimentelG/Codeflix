package com.fullcycle.admin.catalog.infrastructure.api.controllers;

import com.fullcycle.admin.catalog.application.video.create.CreateVideoCommand;
import com.fullcycle.admin.catalog.application.video.create.CreateVideoUseCase;
import com.fullcycle.admin.catalog.application.video.delete.DeleteVideoUseCase;
import com.fullcycle.admin.catalog.application.video.media.get.GetMediaCommand;
import com.fullcycle.admin.catalog.application.video.media.get.GetMediaUseCase;
import com.fullcycle.admin.catalog.application.video.media.upload.UploadMediaCommand;
import com.fullcycle.admin.catalog.application.video.media.upload.UploadMediaUseCase;
import com.fullcycle.admin.catalog.application.video.retrieve.get.GetVideoByIdUseCase;
import com.fullcycle.admin.catalog.application.video.retrieve.list.ListVideosUseCase;
import com.fullcycle.admin.catalog.application.video.update.UpdateVideoCommand;
import com.fullcycle.admin.catalog.application.video.update.UpdateVideoUseCase;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.genre.GenreID;
import com.fullcycle.admin.catalog.domain.pagination.Pagination;
import com.fullcycle.admin.catalog.domain.pagination.VideoSearchQuery;
import com.fullcycle.admin.catalog.domain.resource.Resource;
import com.fullcycle.admin.catalog.domain.video.VideoMediaType;
import com.fullcycle.admin.catalog.domain.video.VideoResource;
import com.fullcycle.admin.catalog.infrastructure.api.VideoAPI;
import com.fullcycle.admin.catalog.infrastructure.utils.ChecksumUtils;
import com.fullcycle.admin.catalog.infrastructure.video.models.*;
import com.fullcycle.admin.catalog.infrastructure.video.presenters.VideoApiPresenter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Set;
import java.util.UUID;

import static com.fullcycle.admin.catalog.domain.utils.CollectionUtils.mapTo;

@RequiredArgsConstructor
@RestController
public class VideoController implements VideoAPI {

	final CreateVideoUseCase createVideoUseCase;
	final UpdateVideoUseCase updateVideoUseCase;
	final ListVideosUseCase listVideosUseCase;
	final GetVideoByIdUseCase getVideoByIdUseCase;
	final GetMediaUseCase getMediaUseCase;
	final UploadMediaUseCase uploadMediaUseCase;
	final DeleteVideoUseCase deleteVideoUseCase;

	@Override
	public ResponseEntity<?> createFull(
	  final String title,
	  final String description,
	  final Integer yearLaunched,
	  final Double duration,
	  final Boolean opened,
	  final Boolean published,
	  final String rating,
	  final Set<String> categories,
	  final Set<String> genres,
	  final Set<String> castMembers,
	  final MultipartFile videoFile,
	  final MultipartFile trailerFile,
	  final MultipartFile bannerFile,
	  final MultipartFile thumbFile,
	  final MultipartFile thumbHalfFile
	) {
		final var command = new CreateVideoCommand(
		  title,
		  description,
		  yearLaunched,
		  duration,
		  opened,
		  published,
		  rating,
		  categories,
		  genres,
		  castMembers,
		  resourceOf(videoFile),
		  resourceOf(trailerFile),
		  resourceOf(bannerFile),
		  resourceOf(thumbFile),
		  resourceOf(thumbHalfFile)
		);

		final var output = createVideoUseCase.execute(command);
		return ResponseEntity.created(URI.create("/videos/" + output.id())).body(output);
	}

	@Override
	public ResponseEntity<?> createPartial(final CreateVideoRequest payload) {
		final var command = new CreateVideoCommand(
		  payload.title(),
		  payload.description(),
		  payload.launchedAt(),
		  payload.duration(),
		  payload.opened(),
		  payload.published(),
		  payload.rating(),
		  payload.categories(),
		  payload.genres(),
		  payload.castMembers(),
		  null,
		  null,
		  null,
		  null,
		  null
		);

		final var output = createVideoUseCase.execute(command);
		return ResponseEntity.created(URI.create("/videos/" + output.id())).body(output);
	}

	@Override
	public ResponseEntity<?> update(UUID id, UpdateVideoRequest payload) {
		final var command = new UpdateVideoCommand(
		  id.toString(),
		  payload.title(),
		  payload.description(),
		  payload.launchedAt(),
		  payload.duration(),
		  payload.opened(),
		  payload.published(),
		  payload.rating(),
		  payload.categories(),
		  payload.genres(),
		  payload.castMembers(),
		  null,
		  null,
		  null,
		  null,
		  null
		);

		final var output = updateVideoUseCase.execute( command);
		return ResponseEntity.ok().body(VideoApiPresenter.present(output));
	}

	@Override
	public VideoResponse getById(final UUID id) {
		return VideoApiPresenter.present(getVideoByIdUseCase.execute(id));
	}

	@Override
	public ResponseEntity<byte[]> getMediaByType(UUID id, String type) {
		final var media = getMediaUseCase.execute(GetMediaCommand.with(id.toString(), type));
		return ResponseEntity.ok()
		  .contentType(MediaType.valueOf(media.contentType()))
		  .contentLength(media.content().length)
		  .header("Content-Disposition", "attachment; filename=\"%s\"".formatted(media.name()))
		  .body(media.content());
	}

	@Override
	public Pagination<VideoListResponse> find(
	  final int page,
	  final int perPage,
	  final String search,
	  final String sort,
	  final String direction,
	  final Set<String> castMembers,
	  final Set<String> categories,
	  final Set<String> genres
	) {
		final var query = new VideoSearchQuery(page, perPage, search, sort, direction, mapTo(categories, CategoryID::from), mapTo(genres, GenreID::from),  mapTo(castMembers, CastMemberID::from));
		return VideoApiPresenter.present(listVideosUseCase.execute(query));
	}

	@Override
	public ResponseEntity<?> updateMedia(final UUID id, final String type, final MultipartFile file) {
		final var mediaType = VideoMediaType.of(type).orElseThrow(() -> new IllegalArgumentException("Invalid media type: " + type));
		final var command = UploadMediaCommand.with(id.toString(), VideoResource.with(resourceOf(file), mediaType));
		return ResponseEntity.created(URI.create("/videos/" + id + "/medias/" + type))
		  .body(VideoApiPresenter.present(uploadMediaUseCase.execute(command)));
	}

	@Override
	public void delete(final UUID id) {
		deleteVideoUseCase.execute(id);
	}

	private Resource resourceOf(final MultipartFile file) {
		if (file == null) {
			return null;
		}

		try {
			return Resource.with(ChecksumUtils.generate(file.getBytes()), file.getBytes(), file.getContentType(), file.getOriginalFilename());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

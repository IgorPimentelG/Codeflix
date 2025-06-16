package com.fullcycle.admin.catalog.infrastructure.api;

import com.fullcycle.admin.catalog.domain.pagination.Pagination;
import com.fullcycle.admin.catalog.infrastructure.video.models.CreateVideoRequest;
import com.fullcycle.admin.catalog.infrastructure.video.models.UpdateVideoRequest;
import com.fullcycle.admin.catalog.infrastructure.video.models.VideoListResponse;
import com.fullcycle.admin.catalog.infrastructure.video.models.VideoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.UUID;

@RequestMapping("/videos")
@Tag(name = "Videos")
public interface VideoAPI {

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Create a new video with all medias")
	@ApiResponses(value = {
	  @ApiResponse(responseCode = "201", description = "Created successfully"),
	  @ApiResponse(responseCode = "422", description = "Unprocessable error"),
	  @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
	})
	ResponseEntity<?> createFull(
	  @RequestParam("title") final String title,
	  @RequestParam("description") final String description,
	  @RequestParam("year_launched") final Integer yearLaunched,
	  @RequestParam("duration") final Double duration,
	  @RequestParam("opened") final Boolean opened,
	  @RequestParam("published") final Boolean published,
	  @RequestParam("rating") final String rating,
	  @RequestParam("categories_id") final Set<String> categories,
	  @RequestParam("genres_id") final Set<String> genres,
	  @RequestParam("cast_members_id") final Set<String> castMembers,
	  @RequestParam("video_file") final MultipartFile videoFile,
	  @RequestParam("trailer_file") final MultipartFile trailerFile,
	  @RequestParam("banner_file") final MultipartFile bannerFile,
	  @RequestParam("thumb_file") final MultipartFile thumbFile,
	  @RequestParam("thumb_half_file") final MultipartFile thumbHalfFile
	);

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Create a new video without medias")
	@ApiResponses(value = {
	  @ApiResponse(responseCode = "201", description = "Created successfully"),
	  @ApiResponse(responseCode = "422", description = "Unprocessable error"),
	  @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
	})
	ResponseEntity<?> createPartial(@RequestBody CreateVideoRequest payload);

	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Update a video by it's identifier")
	@ApiResponses(value = {
	  @ApiResponse(responseCode = "200", description = "Video updated successfully"),
	  @ApiResponse(responseCode = "404", description = "Video was not found"),
	  @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
	})
	ResponseEntity<?> update(@PathVariable("id") UUID id, @RequestBody UpdateVideoRequest payload);

	@GetMapping(value =  "/{id}")
	@Operation(summary = "Get a video by it's identifier")
	@ApiResponses(value = {
	  @ApiResponse(responseCode = "200", description = "Video retrieved successfully"),
	  @ApiResponse(responseCode = "404", description = "Video was not found"),
	  @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
	})
	VideoResponse getById(@PathVariable("id") UUID id);

	@GetMapping(value = "/{id}/medias/{type}")
	@Operation(summary = "Get a video media by it`s type")
	@ApiResponses(value = {
	  @ApiResponse(responseCode = "200", description = "Media retrieved successfully"),
	  @ApiResponse(responseCode = "404", description = "Media was not found"),
	  @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
	})
	ResponseEntity<byte[]> getMediaByType(@PathVariable("id") UUID id, @PathVariable("type") String type);

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Get all videos")
	@ApiResponses(value = {
	  @ApiResponse(responseCode = "200", description = "Videos retrieved successfully"),
	  @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
	})
	Pagination<VideoListResponse> find(
	  @RequestParam(value = "page", defaultValue = "0") int page,
	  @RequestParam(value = "per_page", defaultValue = "25") int perPage,
	  @RequestParam(value = "search", defaultValue = "") String search,
	  @RequestParam(value = "sort", defaultValue = "title") String sort,
	  @RequestParam(value = "direction", defaultValue = "asc") String direction,
	  @RequestParam(value = "cast_members_ids", defaultValue = "") Set<String> castMembers,
	  @RequestParam(value = "categories_ids", defaultValue = "") Set<String> categories,
	  @RequestParam(value = "genres_ids", defaultValue = "") Set<String> genres
	);

	@PostMapping(value = "/{id}/medias/{type}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "Update a video media by it's identifier")
	@ApiResponses(value = {
	  @ApiResponse(responseCode = "201", description = "Media updated successfully"),
	  @ApiResponse(responseCode = "404", description = "Media was not found"),
	  @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
	})
	ResponseEntity<?> updateMedia(
	  @PathVariable("id") UUID id,
	  @PathVariable("type") String type,
	  @RequestParam("media_file") MultipartFile file
	);

	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Delete a video by it's identifier")
	@ApiResponses(value = {
	  @ApiResponse(responseCode = "200", description = "Video deleted successfully"),
	  @ApiResponse(responseCode = "404", description = "Video was not found"),
	  @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
	})
	void delete(@PathVariable("id") UUID id);
}

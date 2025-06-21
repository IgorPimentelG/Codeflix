package com.fullcycle.admin.catalog.application.video.media.upload;

import com.fullcycle.admin.catalog.application.Fixture;
import com.fullcycle.admin.catalog.application.UseCaseTest;
import com.fullcycle.admin.catalog.domain.errors.NotFoundException;
import com.fullcycle.admin.catalog.domain.video.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UploadMediaUseCaseTest extends UseCaseTest {

	@Mock
	private MediaResourceGateway mediaResourceGateway;

	@Mock
	private VideoGateway videoGateway;

	@InjectMocks
	private DefaultUploadMediaUseCase useCase;

	@Override
	protected List<Object> getMocks() {
		return List.of(mediaResourceGateway, videoGateway);
	}

	@Test
	public void givenCmdToUpload_whenIsValid_shouldUpdateVideoMediaAndPersist() {
		final var video = Fixture.Videos.video();
		final var expectedId = video.getId();
		final var expectedType = VideoMediaType.VIDEO;
		final var expectedResource = Fixture.Videos.resource(expectedType);
		final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
		final var expectedMedia = AudioVideoMedia.with(UUID.randomUUID().toString(), expectedType.name(), "/location");

		when(videoGateway.findById(any())).thenReturn(Optional.of(video));
		when(mediaResourceGateway.storeAudioVideo(any(), any())).thenReturn(expectedMedia);
		when(videoGateway.update(any())).thenAnswer(returnsFirstArg());

		final var command = UploadMediaCommand.with(expectedId.toString(), expectedVideoResource);
		final var output = useCase.execute(command);

		assertEquals(expectedType, output.mediaType());
		assertEquals(expectedId.toString(), output.videoID());

		verify(videoGateway, times(1)).findById(eq(expectedId));
		verify(mediaResourceGateway, times(1)).storeAudioVideo(eq(expectedId), eq(expectedVideoResource));
		verify(videoGateway, times(1)).update(argThat(arg ->
		  Objects.equals(expectedMedia, arg.getVideo().get()) &&
		  arg.getTrailer().isEmpty() &&
		  arg.getBanner().isEmpty() &&
		  arg.getThumbnail().isEmpty() &&
		  arg.getThumbnailHalf().isEmpty()
		));
	}

	@Test
	public void givenCmdToUpload_whenIsValid_shouldUpdateTrailerMediaAndPersist() {
		final var video = Fixture.Videos.video();
		final var expectedId = video.getId();
		final var expectedType = VideoMediaType.TRAILER;
		final var expectedResource = Fixture.Videos.resource(expectedType);
		final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
		final var expectedMedia = AudioVideoMedia.with(UUID.randomUUID().toString(), expectedType.name(), "/location");

		when(videoGateway.findById(any())).thenReturn(Optional.of(video));
		when(mediaResourceGateway.storeAudioVideo(any(), any())).thenReturn(expectedMedia);
		when(videoGateway.update(any())).thenAnswer(returnsFirstArg());

		final var command = UploadMediaCommand.with(expectedId.toString(), expectedVideoResource);
		final var output = useCase.execute(command);

		assertEquals(expectedType, output.mediaType());
		assertEquals(expectedId.toString(), output.videoID());

		verify(videoGateway, times(1)).findById(eq(expectedId));
		verify(mediaResourceGateway, times(1)).storeAudioVideo(eq(expectedId), eq(expectedVideoResource));
		verify(videoGateway, times(1)).update(argThat(arg ->
		  Objects.equals(expectedMedia, arg.getTrailer().get()) &&
			arg.getVideo().isEmpty() &&
			arg.getBanner().isEmpty() &&
			arg.getThumbnail().isEmpty() &&
			arg.getThumbnailHalf().isEmpty()
		));
	}

	@Test
	public void givenCmdToUpload_whenIsValid_shouldUpdateBannerMediaAndPersist() {
		final var video = Fixture.Videos.video();
		final var expectedId = video.getId();
		final var expectedType = VideoMediaType.BANNER;
		final var expectedResource = Fixture.Videos.resource(expectedType);
		final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
		final var expectedMedia = ImageMedia.with(UUID.randomUUID().toString(), expectedType.name(), "/location");

		when(videoGateway.findById(any())).thenReturn(Optional.of(video));
		when(mediaResourceGateway.storeImage(any(), any())).thenReturn(expectedMedia);
		when(videoGateway.update(any())).thenAnswer(returnsFirstArg());

		final var command = UploadMediaCommand.with(expectedId.toString(), expectedVideoResource);
		final var output = useCase.execute(command);

		assertEquals(expectedType, output.mediaType());
		assertEquals(expectedId.toString(), output.videoID());

		verify(videoGateway, times(1)).findById(eq(expectedId));
		verify(mediaResourceGateway, times(1)).storeImage(eq(expectedId), eq(expectedVideoResource));
		verify(videoGateway, times(1)).update(argThat(arg ->
		  Objects.equals(expectedMedia, arg.getBanner().get()) &&
			arg.getVideo().isEmpty() &&
			arg.getTrailer().isEmpty() &&
			arg.getThumbnail().isEmpty() &&
			arg.getThumbnailHalf().isEmpty()
		));
	}

	@Test
	public void givenCmdToUpload_whenIsValid_shouldUpdateThumbnailMediaAndPersist() {
		final var video = Fixture.Videos.video();
		final var expectedId = video.getId();
		final var expectedType = VideoMediaType.THUMBNAIL;
		final var expectedResource = Fixture.Videos.resource(expectedType);
		final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
		final var expectedMedia = ImageMedia.with(UUID.randomUUID().toString(), expectedType.name(), "/location");

		when(videoGateway.findById(any())).thenReturn(Optional.of(video));
		when(mediaResourceGateway.storeImage(any(), any())).thenReturn(expectedMedia);
		when(videoGateway.update(any())).thenAnswer(returnsFirstArg());

		final var command = UploadMediaCommand.with(expectedId.toString(), expectedVideoResource);
		final var output = useCase.execute(command);

		assertEquals(expectedType, output.mediaType());
		assertEquals(expectedId.toString(), output.videoID());

		verify(videoGateway, times(1)).findById(eq(expectedId));
		verify(mediaResourceGateway, times(1)).storeImage(eq(expectedId), eq(expectedVideoResource));
		verify(videoGateway, times(1)).update(argThat(arg ->
		  Objects.equals(expectedMedia, arg.getThumbnail().get()) &&
			arg.getVideo().isEmpty() &&
			arg.getBanner().isEmpty() &&
			arg.getTrailer().isEmpty() &&
			arg.getThumbnailHalf().isEmpty()
		));
	}

	@Test
	public void givenCmdToUpload_whenIsValid_shouldUpdateThumbnailHalfMediaAndPersist() {
		final var video = Fixture.Videos.video();
		final var expectedId = video.getId();
		final var expectedType = VideoMediaType.THUMBNAIL_HALF;
		final var expectedResource = Fixture.Videos.resource(expectedType);
		final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
		final var expectedMedia = ImageMedia.with(UUID.randomUUID().toString(), expectedType.name(), "/location");

		when(videoGateway.findById(any())).thenReturn(Optional.of(video));
		when(mediaResourceGateway.storeImage(any(), any())).thenReturn(expectedMedia);
		when(videoGateway.update(any())).thenAnswer(returnsFirstArg());

		final var command = UploadMediaCommand.with(expectedId.toString(), expectedVideoResource);
		final var output = useCase.execute(command);

		assertEquals(expectedType, output.mediaType());
		assertEquals(expectedId.toString(), output.videoID());

		verify(videoGateway, times(1)).findById(eq(expectedId));
		verify(mediaResourceGateway, times(1)).storeImage(eq(expectedId), eq(expectedVideoResource));
		verify(videoGateway, times(1)).update(argThat(arg ->
		  Objects.equals(expectedMedia, arg.getThumbnailHalf().get()) &&
			arg.getVideo().isEmpty() &&
			arg.getBanner().isEmpty() &&
			arg.getTrailer().isEmpty() &&
			arg.getThumbnail().isEmpty()
		));
	}

	@Test
	public void givenCmdToUpload_whenVideoIsInvalid_shouldReturnNotFound() {
		final var video = Fixture.Videos.video();
		final var expectedId = video.getId();
		final var expectedType = VideoMediaType.VIDEO;
		final var expectedResource = Fixture.Videos.resource(expectedType);
		final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
		final var expectedErrorMessage = "Video with ID %s was not found".formatted(expectedId.toString());

		when(videoGateway.findById(any())).thenReturn(Optional.empty());

		final var command = UploadMediaCommand.with(expectedId.toString(), expectedVideoResource);
		final var output = assertThrows(NotFoundException.class, () -> useCase.execute(command));

		assertEquals(expectedErrorMessage, output.getMessage());
	}
}

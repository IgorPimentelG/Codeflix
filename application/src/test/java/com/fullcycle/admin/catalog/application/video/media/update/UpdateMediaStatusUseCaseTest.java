package com.fullcycle.admin.catalog.application.video.media.update;

import com.fullcycle.admin.catalog.application.Fixture;
import com.fullcycle.admin.catalog.application.UseCaseTest;
import com.fullcycle.admin.catalog.domain.errors.NotFoundException;
import com.fullcycle.admin.catalog.domain.video.*;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UpdateMediaStatusUseCaseTest extends UseCaseTest {

	@Mock
	private VideoGateway videoGateway;

	@InjectMocks
	private DefaultUpdateMediaStatusUseCase useCase;

	@Override
	protected List<Object> getMocks() {
		return List.of(videoGateway);
	}

	@Test
	public void givenCommandForVideo_whenIsValid_shouldUpdateStatusAndEncodedLocation() {
		final var expectedStatus = MediaStatus.COMPLETED;
		final var expectedFolder = "encoded_media";
		final var expectedFilename = "filename.mp4";
		final var expectedType = VideoMediaType.VIDEO;
		final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

		final var video = Fixture.Videos.video().setVideo(expectedMedia);
		final var expectedId = video.getId();

		when(videoGateway.findById(any())).thenReturn(Optional.of(video));
		when(videoGateway.update(any())).thenAnswer(returnsFirstArg());

		final var command = UpdateMediaStatusCommand.with(
		  expectedStatus,
		  expectedId.getValue(),
		  expectedMedia.getChecksum(),
		  expectedFolder,
		  expectedFilename
		);

		useCase.execute(command);

		final var captor = ArgumentCaptor.forClass(Video.class);

		verify(videoGateway, times(1)).update(captor.capture());

		final var videoArg = captor.getValue();
		final var videoMediaArg = videoArg.getVideo().get();

		assertEquals(expectedMedia.getChecksum(), videoMediaArg.getChecksum());
		assertEquals(expectedMedia.getRawLocation(), videoMediaArg.getRawLocation());
		assertEquals(expectedStatus, videoMediaArg.getStatus());
		assertEquals(expectedFolder.concat("/").concat(expectedFilename), videoMediaArg.getEncodedLocation());
	}

	@Test
	public void givenCommandForTrailer_whenIsValid_shouldUpdateStatusAndEncodedLocation() {
		final var expectedStatus = MediaStatus.COMPLETED;
		final var expectedFolder = "encoded_media";
		final var expectedFilename = "filename.mp4";
		final var expectedType = VideoMediaType.TRAILER;
		final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

		final var video = Fixture.Videos.video().setTrailer(expectedMedia);
		final var expectedId = video.getId();

		when(videoGateway.findById(any())).thenReturn(Optional.of(video));
		when(videoGateway.update(any())).thenAnswer(returnsFirstArg());

		final var command = UpdateMediaStatusCommand.with(
		  expectedStatus,
		  expectedId.getValue(),
		  expectedMedia.getChecksum(),
		  expectedFolder,
		  expectedFilename
		);

		useCase.execute(command);

		final var captor = ArgumentCaptor.forClass(Video.class);

		verify(videoGateway, times(1)).update(captor.capture());

		final var videoArg = captor.getValue();
		final var videoMediaArg = videoArg.getTrailer().get();

		assertEquals(expectedMedia.getChecksum(), videoMediaArg.getChecksum());
		assertEquals(expectedMedia.getRawLocation(), videoMediaArg.getRawLocation());
		assertEquals(expectedStatus, videoMediaArg.getStatus());
		assertEquals(expectedFolder.concat("/").concat(expectedFilename), videoMediaArg.getEncodedLocation());
	}

	@Test
	public void givenCommandForTrailer_whenInvalid_shouldDoNothing() {
		final var expectedStatus = MediaStatus.COMPLETED;
		final var expectedFolder = "encoded_media";
		final var expectedFilename = "filename.mp4";
		final var expectedType = VideoMediaType.TRAILER;
		final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

		when(videoGateway.findById(any())).thenReturn(Optional.empty());

		final var command = UpdateMediaStatusCommand.with(
		  expectedStatus,
		  VideoID.unique().getValue(),
		  expectedMedia.getChecksum(),
		  expectedFolder,
		  expectedFilename
		);

		assertThrows(NotFoundException.class, () -> useCase.execute(command));
		verify(videoGateway, times(0)).update(any());
	}
}

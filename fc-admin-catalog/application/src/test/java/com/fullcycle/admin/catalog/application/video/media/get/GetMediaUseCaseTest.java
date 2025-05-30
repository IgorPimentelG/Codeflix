package com.fullcycle.admin.catalog.application.video.media.get;

import com.fullcycle.admin.catalog.application.UseCaseTest;
import com.fullcycle.admin.catalog.domain.errors.NotFoundException;
import com.fullcycle.admin.catalog.domain.resource.Resource;
import com.fullcycle.admin.catalog.domain.video.MediaResourceGateway;
import com.fullcycle.admin.catalog.domain.video.VideoID;
import com.fullcycle.admin.catalog.domain.video.VideoMediaType;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class GetMediaUseCaseTest extends UseCaseTest  {

	@Mock
	private MediaResourceGateway mediaResourceGateway;

	@InjectMocks
	private DefaultGetMediaUseCase useCase;

	@Override
	protected List<Object> getMocks() {
		return List.of(mediaResourceGateway);
	}

	@Test
	public void givenVideoIdAndType_whenIsValidCommand_shouldReturnResource() {
		final var expectedId = VideoID.unique();
		final var expectedType = VideoMediaType.VIDEO;
		final var expectedResource = Resource.with(UUID.randomUUID().toString(), "content".getBytes(), "content-type", expectedType.name());

		when(mediaResourceGateway.getResource(expectedId, expectedType)).thenReturn(Optional.of(expectedResource));

		final var command = GetMediaCommand.with(expectedId.toString(), expectedType.name());
		final var output = useCase.execute(command);

		assertEquals(expectedResource.name(), output.name());
		assertEquals(expectedResource.checksum(), output.checksum());
		assertEquals(expectedResource.contentType(), output.contentType());
		assertEquals(expectedResource.content(), output.content());
	}

	@Test
	public void givenVideoIdAndType_whenIsNotFound_shouldReturnNotFoundException() {
		final var expectedId = VideoID.unique();
		final var expectedType = VideoMediaType.VIDEO;

		when(mediaResourceGateway.getResource(expectedId, expectedType)).thenReturn(Optional.empty());

		final var command = GetMediaCommand.with(expectedId.toString(), expectedType.name());
		assertThrows(NotFoundException.class, () -> useCase.execute(command));
	}

	@Test
	public void givenVideoIdAndType_whenTypeDoesntExists_shouldReturnNotFoundException() {
		final var expectedId = VideoID.unique();
		final var expectedErrorMessage = "Media type any-type doesn't exists";

		final var command = GetMediaCommand.with(expectedId.toString(), "any-type");
		final var error = assertThrows(NotFoundException.class, () -> useCase.execute(command));

		assertEquals(expectedErrorMessage, error.getMessage());
	}
}

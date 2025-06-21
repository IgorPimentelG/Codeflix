package com.fullcycle.admin.catalog.infrastructure.amqp;

import com.fullcycle.admin.catalog.AmqpTest;
import com.fullcycle.admin.catalog.application.video.media.update.UpdateMediaStatusCommand;
import com.fullcycle.admin.catalog.application.video.media.update.UpdateMediaStatusUseCase;
import com.fullcycle.admin.catalog.infrastructure.configuration.annotations.VideoEncodedQueue;
import com.fullcycle.admin.catalog.infrastructure.configuration.json.Json;
import com.fullcycle.admin.catalog.infrastructure.configuration.properties.amqp.QueueProperties;
import com.fullcycle.admin.catalog.infrastructure.video.models.VideoEncoderCompleted;
import com.fullcycle.admin.catalog.infrastructure.video.models.VideoEncoderError;
import com.fullcycle.admin.catalog.infrastructure.video.models.VideoMessage;
import com.fullcycle.admin.catalog.infrastructure.video.models.VideoMetadata;
import org.hibernate.sql.ast.tree.expression.Over;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@AmqpTest
public class VideoEncoderListenerTest {

	@Autowired
	private TestRabbitTemplate rabbitTemplate;

	@Autowired
	private RabbitListenerTestHarness harness;

	@MockitoBean
	private UpdateMediaStatusUseCase updateMediaStatusUseCase;

	@Autowired
	@VideoEncodedQueue
	private QueueProperties queueProperties;

	@Test
	public void givenErrorResult_whenCallsListener_shouldProcess() throws InterruptedException {
		final var expectedError = new VideoEncoderError(
		  new VideoMessage(UUID.randomUUID().toString(), "any.mp4"),
		  "Video not found"
		);

		final var expectedMessage = Json.writeValueAsString(expectedError);

		rabbitTemplate.convertAndSend(queueProperties.getQueue(), expectedMessage);

		final var invocation = harness.getNextInvocationDataFor(VideoEncoderListener.LISTENER_ID, 1, TimeUnit.SECONDS);

		assertNotNull(invocation);
		assertNotNull(invocation.getArguments());

		final var message = (String) invocation.getArguments()[0];
		assertEquals(expectedMessage, message);
	}

	@Test
	public void givenCompleteResult_whenCallsListener_shouldCallUseCase() throws InterruptedException {
		final var expectedId = UUID.randomUUID().toString();
		final var expectedOutputBucket = "output-bucket";
		final var expectedStatus = "COMPLETED";
		final var expectedEncoderVideoFolder = "output-folder";
		final var expectedResourceId = UUID.randomUUID().toString();
		final var expectedFilePath = "any.mp4";
		final var expectedVideoMetadata = new VideoMetadata(expectedEncoderVideoFolder, expectedResourceId, expectedFilePath);

		final var expectedCompleted = new VideoEncoderCompleted(expectedId, expectedOutputBucket, expectedVideoMetadata);
		final var expectedMessage = Json.writeValueAsString(expectedCompleted);

		Mockito.doNothing().when(updateMediaStatusUseCase).execute(any());

		rabbitTemplate.convertAndSend(queueProperties.getQueue(), expectedMessage);

		final var invocation = harness.getNextInvocationDataFor(VideoEncoderListener.LISTENER_ID, 1, TimeUnit.SECONDS);

		assertNotNull(invocation);
		assertNotNull(invocation.getArguments());

		final var message = (String) invocation.getArguments()[0];
		assertEquals(expectedMessage, message);

		final var captor = ArgumentCaptor.forClass(UpdateMediaStatusCommand.class);
		verify(updateMediaStatusUseCase).execute(captor.capture());

		final var command = captor.getValue();
		assertEquals(expectedStatus, command.status().name());
		assertEquals(expectedId, command.videoID().toString());
		assertEquals(expectedResourceId, command.checksum());
		assertEquals(expectedEncoderVideoFolder, command.folder());
		assertEquals(expectedFilePath, command.filename());
	}
}

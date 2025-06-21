package com.fullcycle.admin.catalog.infrastructure.amqp;

import com.fullcycle.admin.catalog.application.video.media.update.UpdateMediaStatusCommand;
import com.fullcycle.admin.catalog.application.video.media.update.UpdateMediaStatusUseCase;
import com.fullcycle.admin.catalog.domain.video.MediaStatus;
import com.fullcycle.admin.catalog.infrastructure.configuration.json.Json;
import com.fullcycle.admin.catalog.infrastructure.video.models.VideoEncoderCompleted;
import com.fullcycle.admin.catalog.infrastructure.video.models.VideoEncoderError;
import com.fullcycle.admin.catalog.infrastructure.video.models.VideoEncoderResult;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class VideoEncoderListener {

	private static final Logger log = LoggerFactory.getLogger(VideoEncoderListener.class);
	public static final String LISTENER_ID = "videoEncodedListener";

	private final UpdateMediaStatusUseCase updateMediaStatusUseCase;

	@RabbitListener(id = LISTENER_ID, queues = "${amqp.queues.video-encoded.queue}")
	public void onVideoEncodedMessage(@Payload final String message) {
		final var result = Json.readValue(message, VideoEncoderResult.class);

		if (result instanceof VideoEncoderCompleted dto) {
			final var cmd = new UpdateMediaStatusCommand(
			  MediaStatus.COMPLETED,
			  UUID.fromString(dto.id()),
			  dto.video().resourceId(),
			  dto.video().encodedVideoFolder(),
			  dto.video().filePath()
			);
			updateMediaStatusUseCase.execute(cmd);
		} else if (result instanceof VideoEncoderError dto) {
			log.error("[message:video.listener.income] [status:error] [payload: {}]", message);
		} else {
			log.error("[message:video.listener.income] [status:unknown] [payload: {}]", message);
		}
	}
}

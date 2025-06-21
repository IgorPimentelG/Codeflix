package com.fullcycle.admin.catalog.services.impl;

import com.fullcycle.admin.catalog.AmqpTest;
import com.fullcycle.admin.catalog.domain.video.VideoMediaCreated;
import com.fullcycle.admin.catalog.infrastructure.configuration.annotations.VideoCreatedQueue;
import com.fullcycle.admin.catalog.infrastructure.configuration.json.Json;
import com.fullcycle.admin.catalog.infrastructure.service.EventService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@AmqpTest
public class RabbitEventServiceTest {

	private static final String LISTENER = "video.created";

	@Autowired
	@VideoCreatedQueue
	private EventService publisher;

	@Autowired
	private RabbitListenerTestHarness harness;

	@Component
	static class VideoCreatedNewsListener {
		@RabbitListener(id = LISTENER, queues = "${amqp.queues.video-created.routing-key}")
		void onVideoCreated(@Payload String message) {}
	}

	@Test
	public void shouldSendMessage() throws InterruptedException {
		final var notification = new VideoMediaCreated(UUID.randomUUID(), "file");
		final var expectedMessage = Json.writeValueAsString(notification);

		publisher.send(notification);

		final var invocationData = harness.getNextInvocationDataFor(LISTENER, 1, TimeUnit.SECONDS);

		Assertions.assertNotNull(invocationData);
		Assertions.assertNotNull(invocationData.getArguments());

		final var message = (String) invocationData.getArguments()[0];
		Assertions.assertEquals(expectedMessage, message);
	}
}

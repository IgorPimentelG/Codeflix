package com.fullcycle.admin.catalog.infrastructure.service.impl;

import com.fullcycle.admin.catalog.infrastructure.configuration.json.Json;
import com.fullcycle.admin.catalog.infrastructure.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitOperations;

@RequiredArgsConstructor
public class RabbitEventService implements EventService {

	private final String exchange;
	private final String routingKey;
	private final RabbitOperations operations;

	@Override
	public void send(final Object event) {
		operations.convertAndSend(exchange, routingKey, Json.writeValueAsString(event));
	}
}

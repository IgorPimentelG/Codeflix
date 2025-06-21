package com.fullcycle.admin.catalog.infrastructure.configuration;

import com.fullcycle.admin.catalog.infrastructure.configuration.annotations.VideoCreatedQueue;
import com.fullcycle.admin.catalog.infrastructure.configuration.properties.amqp.QueueProperties;
import com.fullcycle.admin.catalog.infrastructure.service.EventService;
import com.fullcycle.admin.catalog.infrastructure.service.impl.RabbitEventService;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventConfig {

	@Bean
	@VideoCreatedQueue
	public EventService videoCreatedEventService(
	  @VideoCreatedQueue final QueueProperties props,
	  final RabbitOperations operations
	) {
		return new RabbitEventService(props.getExchange(), props.getRoutingKey(), operations);
	}
}

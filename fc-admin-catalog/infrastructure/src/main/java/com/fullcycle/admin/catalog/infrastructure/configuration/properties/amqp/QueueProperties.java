package com.fullcycle.admin.catalog.infrastructure.configuration.properties.amqp;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QueueProperties {

	private String exchange;
	private String routingKey;
	private String queue;
}

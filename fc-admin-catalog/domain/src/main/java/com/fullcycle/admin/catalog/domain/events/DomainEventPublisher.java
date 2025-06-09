package com.fullcycle.admin.catalog.domain.events;

public interface DomainEventPublisher {

	 void publish(DomainEvent event);
}

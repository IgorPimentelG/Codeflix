package com.fullcycle.admin.catalog.domain.video;

import com.fullcycle.admin.catalog.domain.events.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record VideoMediaCreated(UUID resourceID, String filePath, Instant occurredOn) implements DomainEvent {

	public VideoMediaCreated(final UUID resourceID, final String filePath) {
		this(resourceID, filePath, Instant.now());
	}
}

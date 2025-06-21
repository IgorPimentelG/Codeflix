package com.fullcycle.admin.catalog.domain;

import com.fullcycle.admin.catalog.domain.events.DomainEvent;
import com.fullcycle.admin.catalog.domain.events.DomainEventPublisher;
import com.fullcycle.admin.catalog.domain.utils.InstantUtils;
import com.fullcycle.admin.catalog.domain.validation.ValidationHandler;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class EntityTest {

	@Test
	public void givenNullAsEvents_whenInstantiate_shouldBeOk() {
		final List<DomainEvent> events = null;
		final var entity = new DummyEntity();

		assertNotNull(entity.getDomainEvents());
		assertTrue(entity.getDomainEvents().isEmpty());
	}

	@Test
	public void givenDomainEvents_whenPassInConstructor_shouldCreateADefensiveClone() {
		final List<DomainEvent> events = new ArrayList<>();
		events.add((DomainEvent) () -> null);
		final var entity = new DummyEntity(events);

		assertNotNull(entity.getDomainEvents());
		assertEquals(1, entity.getDomainEvents().size());

		assertThrows(RuntimeException.class, () -> {
			final var actualEvents = entity.getDomainEvents();
			actualEvents.add((DomainEvent) () -> null);
		});
	}

	@Test
	public void givenDomainEvents_whenCallsRegisterEvent_shouldAddEventToList() {
		final List<DomainEvent> events = new ArrayList<>();
		final var entity = new DummyEntity(events);

		entity.registerEvent(new DummyEvent());

		assertNotNull(entity.getDomainEvents());
		assertEquals(1, entity.getDomainEvents().size());
	}

	@Test
	public void givenAFewDomainEvents_whenCallsPublishEvent_shouldCallPublisherAndClearTheList() {
		final List<DomainEvent> events = new ArrayList<>();
		final var entity = new DummyEntity(events);
		entity.registerEvent(new DummyEvent());
		entity.registerEvent(new DummyEvent());

		assertNotNull(entity.getDomainEvents());
		assertEquals(2, entity.getDomainEvents().size());

		entity.publishDomainEvents((event) -> {});

		assertEquals(0, entity.getDomainEvents().size());
	}

	public static class DummyEvent implements DomainEvent {

		@Override
		public Instant occurredOn() {
			return Instant.now();
		}
	}

	public static class DummyID extends Identifier {

		private final UUID value;

		public DummyID(final UUID value) {
			this.value = value;
		}

		@Override
		public UUID getValue() {
			return value;
		}

		@Override

		public String toString() {
			return value.toString();
		}
	}

	public static class DummyEntity extends Entity<DummyID> {

		protected DummyEntity() {
			super(new DummyID(UUID.randomUUID()), null);
		}

		protected DummyEntity(List<DomainEvent> events) {
			super(new DummyID(UUID.randomUUID()), events);
		}

		@Override
		public void validate(ValidationHandler handler) {}
	}

}

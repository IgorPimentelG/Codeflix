package com.fullcycle.admin.catalog.domain;

import com.fullcycle.admin.catalog.domain.events.DomainEvent;
import com.fullcycle.admin.catalog.domain.events.DomainEventPublisher;
import com.fullcycle.admin.catalog.domain.validation.ValidationHandler;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
public abstract class Entity<ID extends Identifier> {

    protected final ID id;
    private final List<DomainEvent> domainEvents;

    protected Entity(final ID id) {
        this(id, Collections.emptyList());
    }

    protected Entity(final ID id, final List<DomainEvent> domainEvents) {
        Objects.requireNonNull(id, "ID cannot be null");
        this.id = id;
        this.domainEvents = new ArrayList<>(domainEvents == null ? Collections.emptyList() : domainEvents);
    }

    public abstract void validate(ValidationHandler handler);

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void publishDomainEvents(final DomainEventPublisher publisher) {
        if (publisher == null) return;
        getDomainEvents().forEach(publisher::publish);
        domainEvents.clear();
    }

    public void registerEvent(final DomainEvent event) {
        domainEvents.add(event);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Entity<?> entity = (Entity<?>) o;
        return Objects.equals(id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

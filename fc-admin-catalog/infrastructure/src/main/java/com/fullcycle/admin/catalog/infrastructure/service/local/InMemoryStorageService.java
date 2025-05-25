package com.fullcycle.admin.catalog.infrastructure.service.local;

import com.fullcycle.admin.catalog.domain.resource.Resource;
import com.fullcycle.admin.catalog.infrastructure.service.StorageService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryStorageService implements StorageService {

	private final Map<String, Resource> storage;

	public InMemoryStorageService() {
		this.storage = new ConcurrentHashMap<>();
	}

	public void reset() {
		storage.clear();
	}

	public Map<String, Resource> storage() {
		return storage;
	}

	@Override
	public void store(String name, Resource resource) {
		storage.put(name, resource);
	}

	@Override
	public void deleteAll(Collection<String> names) {
		names.forEach(storage::remove);
	}

	@Override
	public List<String> list(String prefix) {
		if (prefix == null) {
			return Collections.emptyList();
		}

		return storage.keySet().stream()
		  .filter(key -> key.startsWith(prefix))
		  .toList();
	}

	@Override
	public Optional<Resource> get(String name) {
		return Optional.ofNullable(storage.get(name));
	}
}

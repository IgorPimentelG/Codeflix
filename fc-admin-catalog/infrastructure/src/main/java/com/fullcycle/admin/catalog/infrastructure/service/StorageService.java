package com.fullcycle.admin.catalog.infrastructure.service;

import com.fullcycle.admin.catalog.domain.resource.Resource;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface StorageService {

	void store(String name, Resource resource);
	void deleteAll(Collection<String> names);
	List<String> list(String prefix);
	Optional<Resource> get(String name);
}

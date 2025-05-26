package com.fullcycle.admin.catalog.infrastructure.configuration;

import com.fullcycle.admin.catalog.infrastructure.configuration.properties.GCStorageProperties;
import com.fullcycle.admin.catalog.infrastructure.configuration.properties.StorageProperties;
import com.fullcycle.admin.catalog.infrastructure.service.StorageService;
import com.fullcycle.admin.catalog.infrastructure.service.impl.GCStorageService;
import com.fullcycle.admin.catalog.infrastructure.service.local.InMemoryStorageService;
import com.google.cloud.storage.Storage;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class StoreConfig {

	@Bean(name = "storageService")
	@Profile({"development", "production"})
	public StorageService gcStorageService(final GCStorageProperties properties, final Storage storage) {
		return new GCStorageService(properties.getBucket(), storage);
	}

	@Bean(name = "storageService")
	@Profile("test")
	public StorageService inMemoryStorageService() {
		return new InMemoryStorageService();
	}

	@Bean
	@ConfigurationProperties(value = "storage.catalog-video")
	public StorageProperties storageProperties() {
		return new StorageProperties();
	}
}

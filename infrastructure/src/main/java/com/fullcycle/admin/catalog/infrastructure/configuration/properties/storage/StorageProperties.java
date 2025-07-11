package com.fullcycle.admin.catalog.infrastructure.configuration.properties.storage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class StorageProperties {

	private String locationPattern;
	private String filenamePattern;
}

package com.fullcycle.admin.catalog.infrastructure.configuration.properties.storage;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;


@Slf4j
@Getter
@Setter
public class GCStorageProperties implements InitializingBean {

	private String bucket;
	private int connectTimeout;
	private int readTimeout;
	private int retryDelay;
	private int retryMaxDelay;
	private int retryMaxAttempts;
	private double retryMultiplier;

	@Override
	public void afterPropertiesSet() throws Exception {
		log.debug(toString());
	}

	@Override
	public String toString() {
		return "GoogleStorageProperties{" +
		  "bucket='" + bucket + '\'' +
		  ", connectTimeout=" + connectTimeout +
		  ", readTimeout=" + readTimeout +
		  ", retryDelay=" + retryDelay +
		  ", retryMaxDelay=" + retryMaxDelay +
		  ", retryMaxAttempts=" + retryMaxAttempts +
		  ", retryMultiplier=" + retryMultiplier +
		  '}';
	}
}

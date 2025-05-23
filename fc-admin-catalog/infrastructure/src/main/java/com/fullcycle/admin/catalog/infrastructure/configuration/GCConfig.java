package com.fullcycle.admin.catalog.infrastructure.configuration;

import com.fullcycle.admin.catalog.infrastructure.configuration.properties.GCProperties;
import com.fullcycle.admin.catalog.infrastructure.configuration.properties.GCStorageProperties;
import com.google.api.gax.retrying.RetrySettings;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.http.HttpTransportOptions;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.threeten.bp.Duration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

@Configuration
@Profile({"development", "production"})
public class GCConfig {

	@Bean
	@ConfigurationProperties("google.cloud")
	public GCProperties googleCloudProperties() {
		return new GCProperties();
	}

	@Bean
	@ConfigurationProperties("google.cloud.storage.catalog-video")
	public GCStorageProperties googleStorageProperties() {
		return new GCStorageProperties();
	}

	@Bean
	public Credentials credentials(GCProperties props) {
		String credentials = props.getCredentials().trim().replaceAll("\\s+", "");
		final var jsonContent = Base64.getDecoder().decode(credentials);

		try (final var stream = new ByteArrayInputStream(jsonContent)) {
			return GoogleCredentials.fromStream(stream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Bean
	public Storage storage(final Credentials credentials, final GCProperties props, final GCStorageProperties storageProperties) {
		final var transportOptions = HttpTransportOptions.newBuilder()
		  .setConnectTimeout(storageProperties.getConnectTimeout())
		  .setReadTimeout(storageProperties.getReadTimeout())
		  .build();

		final var retrySettings = RetrySettings.newBuilder()
		  .setInitialRetryDelay(Duration.ofMillis(storageProperties.getRetryDelay()))
		  .setMaxRetryDelay(Duration.ofMillis(storageProperties.getRetryMaxDelay()))
		  .setMaxAttempts(storageProperties.getRetryMaxAttempts())
		  .setRetryDelayMultiplier(storageProperties.getRetryMultiplier())
		  .build();

		final var options = StorageOptions.newBuilder()
		  .setCredentials(credentials)
		  .setTransportOptions(transportOptions)
		  .setRetrySettings(retrySettings)
		  .build();

		return options.getService();
	}
}

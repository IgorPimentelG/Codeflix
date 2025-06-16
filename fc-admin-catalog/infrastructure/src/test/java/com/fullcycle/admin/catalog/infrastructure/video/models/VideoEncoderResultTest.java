package com.fullcycle.admin.catalog.infrastructure.video.models;

import com.fullcycle.admin.catalog.JacksonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@JacksonTest
public class VideoEncoderResultTest {

	@Autowired
	private JacksonTester<VideoEncoderResult> json;

	@Test
	public void testUnmarshallSuccessResult() throws IOException {
		final var expectedId = UUID.randomUUID().toString();
		final var expectedOutputBucket = "output-bucket";
		final var expectedStatus = "COMPLETED";
		final var expectedEncoderVideoFolder = "output-folder";
		final var expectedResourceId = UUID.randomUUID().toString();
		final var expectedFilePath = "any.mp4";
		final var expectedVideoMetadata = new VideoMetadata(expectedEncoderVideoFolder, expectedResourceId, expectedFilePath);

		final var json = """
			{
				"id": "%s",
				"output_bucket_path": "%s",
				"status": "%s",
				"video": {
					"encoder_video_folder": "%s",
					"resource_id": "%s",
					"file_path": "%s"
				}
			}
			""".formatted(expectedId, expectedOutputBucket, expectedStatus, expectedEncoderVideoFolder, expectedResourceId, expectedFilePath);

		final var result = this.json.parse(json);

		assertThat(result)
		  .isInstanceOf(VideoEncoderCompleted.class)
		  .hasFieldOrPropertyWithValue("id", expectedId)
		  .hasFieldOrPropertyWithValue("outputBucket", expectedOutputBucket)
		  .hasFieldOrPropertyWithValue("video", expectedVideoMetadata);
	}
	
	@Test
	public void testUnmarshallErrorResult() throws IOException {
		final var expectedMessage = "Resource not found";
		final var expectedStatus = "ERROR";
		final var expectedResourceId = UUID.randomUUID().toString();
		final var expectedFilePath = "any.mp4";
		final var expectedVideoMessage = new VideoMessage(expectedResourceId, expectedFilePath);

		final var json = """
			{
				"status": "%s",
				"error": "%s",
				"message": {
					"resource_id": "%s",
					"file_path": "%s"
				}
			}
			""".formatted(expectedStatus, expectedMessage, expectedResourceId, expectedFilePath);

		final var result = this.json.parse(json);

		assertThat(result)
		  .isInstanceOf(VideoEncoderError.class)
		  .hasFieldOrPropertyWithValue("error", expectedMessage)
		  .hasFieldOrPropertyWithValue("message", expectedVideoMessage);
	}
}

package com.fullcycle.admin.catalog.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VideoMetadata(
  @JsonProperty("encoder_video_folder") String encodedVideoFolder,
  @JsonProperty("resource_id") String resourceId,
  @JsonProperty("file_path") String filePath
) {}

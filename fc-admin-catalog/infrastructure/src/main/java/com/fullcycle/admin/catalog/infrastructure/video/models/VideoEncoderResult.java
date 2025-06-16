package com.fullcycle.admin.catalog.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@VideoResponseTypes
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "status")
public sealed interface VideoEncoderResult permits VideoEncoderCompleted, VideoEncoderError {

	String getStatus();
}

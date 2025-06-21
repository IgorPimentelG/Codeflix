package com.fullcycle.admin.catalog.domain.video;

import java.time.Instant;
import java.util.UUID;

public record VideoPreview(
  UUID id,
  String title,
  String description,
  Instant createdAt,
  Instant updatedAt
) {}

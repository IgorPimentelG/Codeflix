package com.fullcycle.admin.catalog.application.video.create;

import com.fullcycle.admin.catalog.domain.video.Resource;

import java.util.Optional;
import java.util.Set;

public record CreateVideoCommand(
  String title,
  String description,
  Integer launchedAt,
  Double duration,
  Boolean opened,
  Boolean published,
  String rating,
  Set<String> categories,
  Set<String> genres,
  Set<String> members,
  Resource video,
  Resource trailer,
  Resource banner,
  Resource thumbnail,
  Resource thumbnailHalf
) {

    public Optional<Resource> getVideo() {
        return Optional.ofNullable(video);
    }

    public Optional<Resource> getTrailer() {
        return Optional.ofNullable(trailer);
    }

    public Optional<Resource> getBanner() {
        return Optional.ofNullable(banner);
    }

    public Optional<Resource> getThumbnail() {
        return Optional.ofNullable(thumbnail);
    }

    public Optional<Resource> getThumbnailHalf() {
        return Optional.ofNullable(thumbnailHalf);
    }
}

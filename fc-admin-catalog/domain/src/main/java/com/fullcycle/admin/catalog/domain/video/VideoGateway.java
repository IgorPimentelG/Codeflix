package com.fullcycle.admin.catalog.domain.video;

import com.fullcycle.admin.catalog.domain.pagination.Pagination;
import com.fullcycle.admin.catalog.domain.pagination.VideoSearchQuery;

import java.util.Optional;

public interface VideoGateway {
    Video create(Video video);
    Video update(Video video);
    void deleteById(VideoID id);
    Optional<Video> findById(VideoID id);
    Pagination<VideoPreview> findAll(VideoSearchQuery query);
}

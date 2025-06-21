package com.fullcycle.admin.catalog.application.video.delete;

import com.fullcycle.admin.catalog.domain.video.MediaResourceGateway;
import com.fullcycle.admin.catalog.domain.video.VideoGateway;
import com.fullcycle.admin.catalog.domain.video.VideoID;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class DefaultDeleteVideoUseCase extends DeleteVideoUseCase {

    private final VideoGateway videoGateway;
    private final MediaResourceGateway mediaResourceGateway;

    @Override
    public void execute(final UUID id) {
        final var videoId = VideoID.from(id);
        videoGateway.deleteById(videoId);
        mediaResourceGateway.clearResources(videoId);
    }
}

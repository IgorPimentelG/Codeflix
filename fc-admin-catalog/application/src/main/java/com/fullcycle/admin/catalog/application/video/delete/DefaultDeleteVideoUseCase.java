package com.fullcycle.admin.catalog.application.video.delete;

import com.fullcycle.admin.catalog.domain.video.VideoGateway;
import com.fullcycle.admin.catalog.domain.video.VideoID;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class DefaultDeleteVideoUseCase extends DeleteVideoUseCase {

    private final VideoGateway videoGateway;

    @Override
    public void execute(final UUID id) {
        videoGateway.deleteById(VideoID.from(id));
    }
}

package com.fullcycle.admin.catalog.application.video.retrieve.list;

import com.fullcycle.admin.catalog.domain.pagination.Pagination;
import com.fullcycle.admin.catalog.domain.pagination.VideoSearchQuery;
import com.fullcycle.admin.catalog.domain.video.VideoGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultListVideosUseCase extends ListVideosUseCase {

	private final VideoGateway videoGateway;

	@Override
	public Pagination<VideoListOutput> execute(final VideoSearchQuery query) {
		return videoGateway.findAll(query).map(VideoListOutput::from);
	}
}

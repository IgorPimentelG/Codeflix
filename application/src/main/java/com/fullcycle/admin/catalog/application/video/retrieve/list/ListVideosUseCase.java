package com.fullcycle.admin.catalog.application.video.retrieve.list;

import com.fullcycle.admin.catalog.application.UseCase;
import com.fullcycle.admin.catalog.domain.pagination.Pagination;
import com.fullcycle.admin.catalog.domain.pagination.VideoSearchQuery;

public abstract class ListVideosUseCase extends UseCase<VideoSearchQuery, Pagination<VideoListOutput>> {}

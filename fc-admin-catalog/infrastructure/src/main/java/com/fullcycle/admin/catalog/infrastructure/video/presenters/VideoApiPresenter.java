package com.fullcycle.admin.catalog.infrastructure.video.presenters;

import com.fullcycle.admin.catalog.application.video.media.upload.UploadMediaOutput;
import com.fullcycle.admin.catalog.application.video.retrieve.get.VideoOutput;
import com.fullcycle.admin.catalog.application.video.retrieve.list.VideoListOutput;
import com.fullcycle.admin.catalog.application.video.update.UpdateVideoOutput;
import com.fullcycle.admin.catalog.domain.pagination.Pagination;
import com.fullcycle.admin.catalog.domain.video.AudioVideoMedia;
import com.fullcycle.admin.catalog.domain.video.ImageMedia;
import com.fullcycle.admin.catalog.infrastructure.video.models.*;

public interface VideoApiPresenter {

	static VideoResponse present(final VideoOutput output) {
		return new VideoResponse(
		  output.id().toString(),
		  output.title(),
		  output.description(),
		  output.launchedAt(),
		  output.duration(),
		  output.opened(),
		  output.published(),
		  output.rating(),
		  output.createdAt(),
		  output.updatedAt(),
		  present(output.banner()),
		  present(output.thumbnail()),
		  present(output.thumbnailHalf()),
		  present(output.video()),
		  present(output.trailer()),
		  output.categories(),
		  output.genres(),
		  output.members()
		);
	}

	static ImageMediaResponse present(final ImageMedia output) {
		return new ImageMediaResponse(output.getChecksum(), output.getName(), output.getLocation());
	}

	static AudioVideoMediaResponse present(final AudioVideoMedia output) {
		return new AudioVideoMediaResponse(output.getChecksum(), output.getName(), output.getRawLocation(), output.getEncodedLocation(), output.getStatus().name());
	}

	static UpdateVideoResponse present(final UpdateVideoOutput output) {
		return new UpdateVideoResponse(output.id().toString());
	}

	static VideoListResponse present(final VideoListOutput output) {
		return new VideoListResponse(
		  output.id(),
		  output.title(),
		  output.description(),
		  output.createdAt(),
		  output.updatedAt()
		);
	}

	static Pagination<VideoListResponse> present(final Pagination<VideoListOutput> page) {
			return page.map(VideoApiPresenter::present);
	}

	static UploadMediaResponse present(final UploadMediaOutput output) {
		return new UploadMediaResponse(output.videoID(), output.mediaType().name());
	}
}

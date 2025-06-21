package com.fullcycle.admin.catalog.infrastructure.configuration.usecases;

import com.fullcycle.admin.catalog.application.video.create.CreateVideoUseCase;
import com.fullcycle.admin.catalog.application.video.create.DefaultCreateVideoUseCase;
import com.fullcycle.admin.catalog.application.video.delete.DefaultDeleteVideoUseCase;
import com.fullcycle.admin.catalog.application.video.delete.DeleteVideoUseCase;
import com.fullcycle.admin.catalog.application.video.media.get.DefaultGetMediaUseCase;
import com.fullcycle.admin.catalog.application.video.media.get.GetMediaUseCase;
import com.fullcycle.admin.catalog.application.video.media.update.DefaultUpdateMediaStatusUseCase;
import com.fullcycle.admin.catalog.application.video.media.update.UpdateMediaStatusUseCase;
import com.fullcycle.admin.catalog.application.video.media.upload.DefaultUploadMediaUseCase;
import com.fullcycle.admin.catalog.application.video.media.upload.UploadMediaUseCase;
import com.fullcycle.admin.catalog.application.video.retrieve.get.DefaultGetVideoByIdUseCase;
import com.fullcycle.admin.catalog.application.video.retrieve.get.GetVideoByIdUseCase;
import com.fullcycle.admin.catalog.application.video.retrieve.list.DefaultListVideosUseCase;
import com.fullcycle.admin.catalog.application.video.retrieve.list.ListVideosUseCase;
import com.fullcycle.admin.catalog.application.video.update.DefaultUpdateVideoUseCase;
import com.fullcycle.admin.catalog.application.video.update.UpdateVideoUseCase;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalog.domain.category.CategoryGateway;
import com.fullcycle.admin.catalog.domain.genre.GenreGateway;
import com.fullcycle.admin.catalog.domain.video.MediaResourceGateway;
import com.fullcycle.admin.catalog.domain.video.VideoGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class VideoUseCaseConfig {

	private final VideoGateway videoGateway;
	private final CategoryGateway categoryGateway;
	private final CastMemberGateway castMemberGateway;
	private final GenreGateway genreGateway;
	private final MediaResourceGateway mediaResourceGateway;

	@Bean
	public UpdateMediaStatusUseCase updateMediaStatusUseCase() {
		return new DefaultUpdateMediaStatusUseCase(videoGateway);
	}

	@Bean
	public UploadMediaUseCase uploadMediaUseCase() {
		return new DefaultUploadMediaUseCase(videoGateway, mediaResourceGateway);
	}

	@Bean
	public CreateVideoUseCase createVideoUseCase() {
		return new DefaultCreateVideoUseCase(videoGateway, genreGateway, categoryGateway, castMemberGateway, mediaResourceGateway);
	}

	@Bean
	public UpdateVideoUseCase updateVideoUseCase() {
		return new DefaultUpdateVideoUseCase(videoGateway, categoryGateway, genreGateway, castMemberGateway, mediaResourceGateway);
	}

	@Bean
	public GetVideoByIdUseCase getVideoByIdUseCase() {
		return new DefaultGetVideoByIdUseCase(videoGateway);
	}

	@Bean
	public GetMediaUseCase getMediaUseCase() {
		return new DefaultGetMediaUseCase(mediaResourceGateway);
	}

	@Bean
	public ListVideosUseCase listVideosUseCase() {
		return new DefaultListVideosUseCase(videoGateway);
	}

	@Bean
	public DeleteVideoUseCase deleteVideoUseCase() {
		return new DefaultDeleteVideoUseCase(videoGateway, mediaResourceGateway);
	}
}

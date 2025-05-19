package com.fullcycle.admin.catalog.infrastructure.video;

import com.fullcycle.admin.catalog.domain.Identifier;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalog.domain.pagination.Pagination;
import com.fullcycle.admin.catalog.domain.pagination.VideoSearchQuery;
import com.fullcycle.admin.catalog.domain.video.Video;
import com.fullcycle.admin.catalog.domain.video.VideoGateway;
import com.fullcycle.admin.catalog.domain.video.VideoID;
import com.fullcycle.admin.catalog.infrastructure.utils.SQLUtils;
import com.fullcycle.admin.catalog.infrastructure.video.persistence.VideoJpaEntity;
import com.fullcycle.admin.catalog.infrastructure.video.persistence.VideoPreview;
import com.fullcycle.admin.catalog.infrastructure.video.persistence.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VideoMySQLGateway implements VideoGateway {

	private final VideoRepository videoRepository;

	@Override
	@Transactional
	public Video create(final Video video) {
		return save(video);
	}

	@Override
	@Transactional
	public Video update(final Video video) {
		return save(video);
	}

	@Override
	public void deleteById(final VideoID id) {
		final var videoId = id.getValue();

		if (videoRepository.existsById(videoId)) {
			videoRepository.deleteById(videoId);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Video> findById(final VideoID id) {
		return videoRepository.findById(id.getValue()).map(VideoJpaEntity::toAggregate);
	}

	@Override
	public Pagination<VideoPreview> findAll(final VideoSearchQuery query) {
		final var page = PageRequest.of(
		  query.page(),
		  query.perPage(),
		  Sort.by(Sort.Direction.fromString(query.direction()), query.sort())
		);

		final var videos = videoRepository.findAll(
		  SQLUtils.like(query.terms()),
		  toString(query.categories()),
		  toString(query.genres()),
		  toString(query.castMembers()),
		  page
		);

		return new Pagination<>(
		  videos.getNumber(),
		  videos.getSize(),
		  videos.getTotalElements(),
		  videos.toList()
		);
	}

	private Video save(final Video video) {
		return videoRepository.save(VideoJpaEntity.from(video)).toAggregate();
	}

	private Set<String> toString(final Set<? extends Identifier> ids) {
		if (ids == null || ids.isEmpty()) {
			return null;
		}

		return ids.stream().map(it -> it.getValue().toString()).collect(Collectors.toSet());
	}
}

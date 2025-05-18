package com.fullcycle.admin.catalog.application.video.update;

import com.fullcycle.admin.catalog.domain.Identifier;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalog.domain.category.CategoryGateway;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.errors.DomainException;
import com.fullcycle.admin.catalog.domain.errors.InternalException;
import com.fullcycle.admin.catalog.domain.errors.NotFoundException;
import com.fullcycle.admin.catalog.domain.errors.NotificationException;
import com.fullcycle.admin.catalog.domain.genre.GenreGateway;
import com.fullcycle.admin.catalog.domain.genre.GenreID;
import com.fullcycle.admin.catalog.domain.validation.Error;
import com.fullcycle.admin.catalog.domain.validation.ValidationHandler;
import com.fullcycle.admin.catalog.domain.validation.handler.Notification;
import com.fullcycle.admin.catalog.domain.video.*;
import lombok.RequiredArgsConstructor;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DefaultUpdateVideoUseCase extends UpdateVideoUseCase {

	private final VideoGateway videoGateway;
	private final CategoryGateway categoryGateway;
	private final GenreGateway genreGateway;
	private final CastMemberGateway castMemberGateway;
	private final MediaResourceGateway mediaResourceGateway;

	@Override
	public UpdateVideoOutput execute(final UpdateVideoCommand command) {
		final var id = VideoID.from(command.id());
		final var rating = Rating.of(command.rating()).orElse(null);
		final var launchedAt = command.launchedAt() != null ? Year.of(command.launchedAt()) : null;
		final var categories = toIdentifier(command.categories(), CategoryID::from);
		final var genres = toIdentifier(command.genres(), GenreID::from);
		final var members = toIdentifier(command.members(), CastMemberID::from);

		final var video = videoGateway.findById(id).orElseThrow(notFoundException(id));

		final var notification = Notification.create();
		notification.append(validateCategories(categories));
		notification.append(validateGenres(genres));
		notification.append(validateMembers(members));

		video.update(
		  command.title(),
		  command.description(),
		  launchedAt,
		  command.duration(),
		  rating,
		  command.opened(),
		  command.published(),
		  categories,
		  genres,
		  members
		);

		video.validate(notification);

		if (notification.hasError()) {
			throw new NotificationException(notification);
		}

		return UpdateVideoOutput.from(update(command, video));
	}

	private Video update(UpdateVideoCommand command, Video video) {
		final var id = video.getId();

		try {
			final var videoMedia = command.getVideo().map(it -> mediaResourceGateway.storeAudioVideo(id, it)).orElse(null);
			final var trailerMedia = command.getTrailer().map(it -> mediaResourceGateway.storeAudioVideo(id, it)).orElse(null);
			final var bannerMedia = command.getBanner().map(it -> mediaResourceGateway.storeImage(id, it)).orElse(null);
			final var thumbnailMedia = command.getThumbnail().map(it -> mediaResourceGateway.storeImage(id, it)).orElse(null);
			final var thumbnailHalfMedia = command.getThumbnailHalf().map(it -> mediaResourceGateway.storeImage(id, it)).orElse(null);

			video.setVideo(videoMedia);
			video.setTrailer(trailerMedia);
			video.setBanner(bannerMedia);
			video.setThumbnail(thumbnailMedia);
			video.setThumbnailHalf(thumbnailHalfMedia);

			return videoGateway.update(video);
		} catch (final Throwable t) {
			throw InternalException.with("Error on create video was observed [videoID:%s]".formatted(id.getValue()) ,t);
		}
	}

	private Supplier<DomainException> notFoundException(final VideoID id) {
		return () -> NotFoundException.with(Video.class, id);
	}

	private <T extends Identifier> ValidationHandler validateAggregate(
	  final String aggregate,
	  final Set<T> ids,
	  final Function<Iterable<T>, List<T>> existsByIds
	) {
		final var notification = Notification.create();

		if (ids == null || ids.isEmpty()) {
			return notification;
		}

		final var retrievedIds = existsByIds.apply(ids);

		if (ids.size() != retrievedIds.size()) {
			final var missingIds = new ArrayList<>(ids);
			missingIds.removeAll(retrievedIds);

			final var missingIdsMessage = missingIds.stream()
			  .map(Identifier::getValue)
			  .map(String::valueOf)
			  .collect(Collectors.joining(", "));

			notification.append(new Error("Some %s could not be found: %s".formatted(aggregate, missingIdsMessage)));
		}

		return notification;
	}

	private ValidationHandler validateCategories(final Set<CategoryID> ids) {
		return validateAggregate("categories", ids, categoryGateway::existsByIds);
	}

	private ValidationHandler validateGenres(final Set<GenreID> ids) {
		return validateAggregate("genres", ids, genreGateway::existsByIds);
	}

	private ValidationHandler validateMembers(final Set<CastMemberID> ids) {
		return validateAggregate("cast members", ids, castMemberGateway::existsByIds);
	}

	private <T> Set<T> toIdentifier(final Set<String> identifiers, final Function<String, T> mapper) {
		return identifiers.stream().map(mapper).collect(Collectors.toSet());
	}
}

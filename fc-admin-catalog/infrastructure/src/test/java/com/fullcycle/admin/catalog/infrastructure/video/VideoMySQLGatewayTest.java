package com.fullcycle.admin.catalog.infrastructure.video;

import com.fullcycle.admin.catalog.Fixture;
import com.fullcycle.admin.catalog.IntegrationTest;
import com.fullcycle.admin.catalog.domain.castmember.CastMember;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalog.domain.category.Category;
import com.fullcycle.admin.catalog.domain.category.CategoryGateway;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.genre.Genre;
import com.fullcycle.admin.catalog.domain.genre.GenreGateway;
import com.fullcycle.admin.catalog.domain.genre.GenreID;
import com.fullcycle.admin.catalog.domain.pagination.VideoSearchQuery;
import com.fullcycle.admin.catalog.domain.video.*;
import com.fullcycle.admin.catalog.infrastructure.video.persistence.VideoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.Set;
import java.util.UUID;

import static com.fullcycle.admin.catalog.domain.utils.CollectionUtils.mapTo;
import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
public class VideoMySQLGatewayTest {

	@Autowired
	private VideoMySQLGateway videoGateway;

	@Autowired
	private CastMemberGateway castMemberGateway;

	@Autowired
	private CategoryGateway categoryGateway;

	@Autowired
	private GenreGateway genreGateway;

	@Autowired
	private VideoRepository videoRepository;

	private static final Category CATEGORY = Fixture.Categories.category();
	private static final Genre GENRE = Fixture.Genres.genre();
	private static final CastMember CAST_MEMBER = Fixture.CastMembers.castMember();

	@Test
	@Transactional
	public void givenValidVideo_whenCallsCreate_shouldPersist() {
		final var castMember = castMemberGateway.create(Fixture.CastMembers.castMember());
		final var category = categoryGateway.create(Fixture.Categories.category());
		final var genre = genreGateway.create(Fixture.Genres.genre());

		final var expectedTitle = Fixture.title();
		final var expectedDescription = Fixture.description();
		final var expectedLaunchedAt = Year.of(Fixture.year());
		final var expectedDuration = Fixture.duration();
		final var expectedOpened = Fixture.bool();
		final var expectedPublished = Fixture.bool();
		final var expectedRating = Fixture.Videos.rating();
		final var expectedCategories = Set.of(category.getId());
		final var expectedGenres = Set.of(genre.getId());
		final var expectedCastMembers = Set.of(castMember.getId());
		final var expectedVideo = AudioVideoMedia.with(UUID.randomUUID().toString(), "any name", "/raw-location", "/encoded-location", MediaStatus.COMPLETED);
		final var expectedTrailer = AudioVideoMedia.with(UUID.randomUUID().toString(), "any name", "/raw-location", "/encoded-location", MediaStatus.COMPLETED);
		final var expectedBanner = ImageMedia.with(UUID.randomUUID().toString(), "any name", "/location");
		final var thumbnail = ImageMedia.with(UUID.randomUUID().toString(), "any name", "/location");
		final var thumbnailHalf = ImageMedia.with(UUID.randomUUID().toString(), "any name", "/location");

		final var video = Video.newVideo(
		  expectedTitle,
		  expectedDescription,
		  expectedLaunchedAt,
		  expectedDuration,
		  expectedRating,
		  expectedOpened,
		  expectedPublished,
		  expectedCategories,
		  expectedGenres,
		  expectedCastMembers
		)
		  .setVideo(expectedVideo)
		  .setTrailer(expectedTrailer)
		  .setBanner(expectedBanner)
		  .setThumbnail(thumbnail)
		  .setThumbnailHalf(thumbnailHalf);

		final var output = videoGateway.create(video);

		assertNotNull(output);
		assertNotNull(output.getId());

		assertEquals(expectedTitle, output.getTitle());
		assertEquals(expectedDescription, output.getDescription());
		assertEquals(expectedLaunchedAt, output.getLaunchedAt());
		assertEquals(expectedDuration, output.getDuration());
		assertEquals(expectedOpened, output.isOpened());
		assertEquals(expectedPublished, output.isPublished());
		assertEquals(expectedRating, output.getRating());
		assertEquals(expectedCategories, output.getCategories());
		assertEquals(expectedGenres, output.getGenres());
		assertEquals(expectedCastMembers, output.getCastMembers());
		assertEquals(expectedVideo, output.getVideo().get());
		assertEquals(expectedTrailer, output.getTrailer().get());
		assertEquals(expectedBanner, output.getBanner().get());
		assertEquals(thumbnail, output.getThumbnail().get());
		assertEquals(thumbnailHalf, output.getThumbnailHalf().get());

		final var persisted = videoRepository.findById(output.getId().getValue()).get();

		assertEquals(expectedTitle, persisted.getTitle());
		assertEquals(expectedDescription, persisted.getDescription());
		assertEquals(expectedLaunchedAt, Year.of(persisted.getYearLaunched()));
		assertEquals(expectedDuration, persisted.getDuration());
		assertEquals(expectedOpened, persisted.isOpened());
		assertEquals(expectedPublished, persisted.isPublished());
		assertEquals(expectedRating, persisted.getRating());
		assertEquals(expectedCategories, mapTo(persisted.getCategories(), (it) -> CategoryID.from(it.getId().getCategoryID())));
		assertEquals(expectedGenres, mapTo(persisted.getGenres(), (it) -> GenreID.from(it.getId().getGenreID())));
		assertEquals(expectedCastMembers, mapTo(persisted.getCastMembers(), (it) -> CastMemberID.from(it.getId().getCastMemberID())));
		assertEquals(expectedVideo.getChecksum(), persisted.getVideo().getChecksum());
		assertEquals(expectedTrailer.getChecksum(), persisted.getTrailer().getChecksum());
		assertEquals(expectedBanner.getChecksum(), persisted.getBanner().getChecksum());
		assertEquals(thumbnail.getChecksum(), persisted.getThumbnail().getChecksum());
		assertEquals(thumbnailHalf.getChecksum(), persisted.getThumbnailHalf().getChecksum());
	}

	@Test
	@Transactional
	public void givenValidVideoWithoutRelations_whenCallsCreate_shouldPersist() {
		final var castMember = castMemberGateway.create(Fixture.CastMembers.castMember());
		final var category = categoryGateway.create(Fixture.Categories.category());
		final var genre = genreGateway.create(Fixture.Genres.genre());

		final var expectedTitle = Fixture.title();
		final var expectedDescription = Fixture.description();
		final var expectedLaunchedAt = Year.of(Fixture.year());
		final var expectedDuration = Fixture.duration();
		final var expectedOpened = Fixture.bool();
		final var expectedPublished = Fixture.bool();
		final var expectedRating = Fixture.Videos.rating();
		final var expectedCategories = Set.<CategoryID>of();
		final var expectedGenres = Set.<GenreID>of();
		final var expectedCastMembers = Set.<CastMemberID>of();
		final var expectedVideo = AudioVideoMedia.with(UUID.randomUUID().toString(), "any name", "/raw-location", "/encoded-location", MediaStatus.COMPLETED);
		final var expectedTrailer = AudioVideoMedia.with(UUID.randomUUID().toString(), "any name", "/raw-location", "/encoded-location", MediaStatus.COMPLETED);
		final var expectedBanner = ImageMedia.with(UUID.randomUUID().toString(), "any name", "/location");
		final var thumbnail = ImageMedia.with(UUID.randomUUID().toString(), "any name", "/location");
		final var thumbnailHalf = ImageMedia.with(UUID.randomUUID().toString(), "any name", "/location");

		final var video = Video.newVideo(
			expectedTitle,
			expectedDescription,
			expectedLaunchedAt,
			expectedDuration,
			expectedRating,
			expectedOpened,
			expectedPublished,
			expectedCategories,
			expectedGenres,
			expectedCastMembers
		  )
		  .setVideo(expectedVideo)
		  .setTrailer(expectedTrailer)
		  .setBanner(expectedBanner)
		  .setThumbnail(thumbnail)
		  .setThumbnailHalf(thumbnailHalf);

		final var output = videoGateway.create(video);

		assertNotNull(output);
		assertNotNull(output.getId());

		assertEquals(expectedTitle, output.getTitle());
		assertEquals(expectedDescription, output.getDescription());
		assertEquals(expectedLaunchedAt, output.getLaunchedAt());
		assertEquals(expectedDuration, output.getDuration());
		assertEquals(expectedOpened, output.isOpened());
		assertEquals(expectedPublished, output.isPublished());
		assertEquals(expectedRating, output.getRating());
		assertEquals(expectedCategories, output.getCategories());
		assertEquals(expectedGenres, output.getGenres());
		assertEquals(expectedCastMembers, output.getCastMembers());
		assertEquals(expectedVideo, output.getVideo().get());
		assertEquals(expectedTrailer, output.getTrailer().get());
		assertEquals(expectedBanner, output.getBanner().get());
		assertEquals(thumbnail, output.getThumbnail().get());
		assertEquals(thumbnailHalf, output.getThumbnailHalf().get());

		final var persisted = videoRepository.findById(output.getId().getValue()).get();

		assertEquals(expectedTitle, persisted.getTitle());
		assertEquals(expectedDescription, persisted.getDescription());
		assertEquals(expectedLaunchedAt, Year.of(persisted.getYearLaunched()));
		assertEquals(expectedDuration, persisted.getDuration());
		assertEquals(expectedOpened, persisted.isOpened());
		assertEquals(expectedPublished, persisted.isPublished());
		assertEquals(expectedRating, persisted.getRating());
		assertTrue(persisted.getCategories().isEmpty());
		assertTrue(persisted.getGenres().isEmpty());
		assertTrue(persisted.getCastMembers().isEmpty());
		assertEquals(expectedVideo.getChecksum(), persisted.getVideo().getChecksum());
		assertEquals(expectedTrailer.getChecksum(), persisted.getTrailer().getChecksum());
		assertEquals(expectedBanner.getChecksum(), persisted.getBanner().getChecksum());
		assertEquals(thumbnail.getChecksum(), persisted.getThumbnail().getChecksum());
		assertEquals(thumbnailHalf.getChecksum(), persisted.getThumbnailHalf().getChecksum());
	}

	@Test
	@Transactional
	public void givenValidVideo_whenCallsUpdate_shouldUpdateIt() {

		final var video = videoGateway.create(Video.newVideo(
			Fixture.title(),
			Fixture.description(),
			Year.of(Fixture.year()),
			Fixture.duration(),
			Fixture.Videos.rating(),
			Fixture.bool(),
			Fixture.bool(),
			Set.of(),
			Set.of(),
			Set.of()
		  ));

		final var castMember = castMemberGateway.create(Fixture.CastMembers.castMember());
		final var category = categoryGateway.create(Fixture.Categories.category());
		final var genre = genreGateway.create(Fixture.Genres.genre());

		final var expectedTitle = Fixture.title();
		final var expectedDescription = Fixture.description();
		final var expectedLaunchedAt = Year.of(Fixture.year());
		final var expectedDuration = Fixture.duration();
		final var expectedOpened = Fixture.bool();
		final var expectedPublished = Fixture.bool();
		final var expectedRating = Fixture.Videos.rating();
		final var expectedCategories = Set.of(category.getId());
		final var expectedGenres = Set.of(genre.getId());
		final var expectedCastMembers = Set.of(castMember.getId());
		final var expectedVideo = AudioVideoMedia.with(UUID.randomUUID().toString(), "any name", "/raw-location", "/encoded-location", MediaStatus.COMPLETED);
		final var expectedTrailer = AudioVideoMedia.with(UUID.randomUUID().toString(), "any name", "/raw-location", "/encoded-location", MediaStatus.COMPLETED);
		final var expectedBanner = ImageMedia.with(UUID.randomUUID().toString(), "any name", "/location");
		final var thumbnail = ImageMedia.with(UUID.randomUUID().toString(), "any name", "/location");
		final var thumbnailHalf = ImageMedia.with(UUID.randomUUID().toString(), "any name", "/location");

		final var updatedVideo = Video.with(video).update(
			expectedTitle,
			expectedDescription,
		    expectedLaunchedAt,
			expectedDuration,
		    expectedRating,
			expectedOpened,
			expectedPublished,
			expectedCategories,
			expectedGenres,
			expectedCastMembers
		)
		  .setVideo(expectedVideo)
		  .setTrailer(expectedTrailer)
		  .setBanner(expectedBanner)
		  .setThumbnail(thumbnail)
		  .setThumbnailHalf(thumbnailHalf);

		final var output = videoGateway.update(updatedVideo);

		assertNotNull(output);
		assertNotNull(output.getId());

		assertEquals(expectedTitle, output.getTitle());
		assertEquals(expectedDescription, output.getDescription());
		assertEquals(expectedLaunchedAt, output.getLaunchedAt());
		assertEquals(expectedDuration, output.getDuration());
		assertEquals(expectedOpened, output.isOpened());
		assertEquals(expectedPublished, output.isPublished());
		assertEquals(expectedRating, output.getRating());
		assertEquals(expectedCategories, output.getCategories());
		assertEquals(expectedGenres, output.getGenres());
		assertEquals(expectedCastMembers, output.getCastMembers());
		assertEquals(expectedVideo, output.getVideo().get());
		assertEquals(expectedTrailer, output.getTrailer().get());
		assertEquals(expectedBanner, output.getBanner().get());
		assertEquals(thumbnail, output.getThumbnail().get());
		assertEquals(thumbnailHalf, output.getThumbnailHalf().get());
		assertNotNull(output.getCreatedAt());
		assertNotNull(output.getUpdatedAt());

		final var persisted = videoRepository.findById(output.getId().getValue()).get();

		assertEquals(expectedTitle, persisted.getTitle());
		assertEquals(expectedDescription, persisted.getDescription());
		assertEquals(expectedLaunchedAt, Year.of(persisted.getYearLaunched()));
		assertEquals(expectedDuration, persisted.getDuration());
		assertEquals(expectedOpened, persisted.isOpened());
		assertEquals(expectedPublished, persisted.isPublished());
		assertEquals(expectedRating, persisted.getRating());
		assertEquals(expectedCategories, mapTo(persisted.getCategories(), (it) -> CategoryID.from(it.getId().getCategoryID())));
		assertEquals(expectedGenres, mapTo(persisted.getGenres(), (it) -> GenreID.from(it.getId().getGenreID())));
		assertEquals(expectedCastMembers, mapTo(persisted.getCastMembers(), (it) -> CastMemberID.from(it.getId().getCastMemberID())));
		assertEquals(expectedVideo.getChecksum(), persisted.getVideo().getChecksum());
		assertEquals(expectedTrailer.getChecksum(), persisted.getTrailer().getChecksum());
		assertEquals(expectedBanner.getChecksum(), persisted.getBanner().getChecksum());
		assertEquals(thumbnail.getChecksum(), persisted.getThumbnail().getChecksum());
		assertEquals(thumbnailHalf.getChecksum(), persisted.getThumbnailHalf().getChecksum());
		assertNotNull(persisted.getCreatedAt());
		assertNotNull(persisted.getUpdatedAt());
	}

	@Test
	public void givenValidVideo_whenCallsDeleteById_shouldDeleteIt() {
		final var video = videoGateway.create(Video.newVideo(
		  Fixture.title(),
		  Fixture.description(),
		  Year.of(Fixture.year()),
		  Fixture.duration(),
		  Fixture.Videos.rating(),
		  Fixture.bool(),
		  Fixture.bool(),
		  Set.of(),
		  Set.of(),
		  Set.of()
		));

		assertEquals(1, videoRepository.count());
		videoGateway.deleteById(video.getId());
		assertEquals(0, videoRepository.count());
	}

	@Test
	public void givenInvalidVideo_whenCallsDeleteById_shouldDeleteIt() {
		final var video = videoGateway.create(Video.newVideo(
		  Fixture.title(),
		  Fixture.description(),
		  Year.of(Fixture.year()),
		  Fixture.duration(),
		  Fixture.Videos.rating(),
		  Fixture.bool(),
		  Fixture.bool(),
		  Set.of(),
		  Set.of(),
		  Set.of()
		));

		assertEquals(1, videoRepository.count());
		videoGateway.deleteById(VideoID.unique());
		assertEquals(1, videoRepository.count());
	}

	@Test
	public void givenValidVideo_whenCallsFindById_shouldReturn() {
		final var castMember = castMemberGateway.create(Fixture.CastMembers.castMember());
		final var category = categoryGateway.create(Fixture.Categories.category());
		final var genre = genreGateway.create(Fixture.Genres.genre());

		final var expectedTitle = Fixture.title();
		final var expectedDescription = Fixture.description();
		final var expectedLaunchedAt = Year.of(Fixture.year());
		final var expectedDuration = Fixture.duration();
		final var expectedOpened = Fixture.bool();
		final var expectedPublished = Fixture.bool();
		final var expectedRating = Fixture.Videos.rating();
		final var expectedCategories = Set.of(category.getId());
		final var expectedGenres = Set.of(genre.getId());
		final var expectedCastMembers = Set.of(castMember.getId());
		final var expectedVideo = AudioVideoMedia.with(UUID.randomUUID().toString(), "any name", "/raw-location", "/encoded-location", MediaStatus.COMPLETED);
		final var expectedTrailer = AudioVideoMedia.with(UUID.randomUUID().toString(), "any name", "/raw-location", "/encoded-location", MediaStatus.COMPLETED);
		final var expectedBanner = ImageMedia.with(UUID.randomUUID().toString(), "any name", "/location");
		final var thumbnail = ImageMedia.with(UUID.randomUUID().toString(), "any name", "/location");
		final var thumbnailHalf = ImageMedia.with(UUID.randomUUID().toString(), "any name", "/location");

		final var video = videoGateway.create(Video.newVideo(
			expectedTitle,
			expectedDescription,
			expectedLaunchedAt,
			expectedDuration,
			expectedRating,
			expectedOpened,
			expectedPublished,
			expectedCategories,
			expectedGenres,
			expectedCastMembers
		  )
		  .setVideo(expectedVideo)
		  .setTrailer(expectedTrailer)
		  .setBanner(expectedBanner)
		  .setThumbnail(thumbnail)
		  .setThumbnailHalf(thumbnailHalf));

		final var output = videoGateway.findById(video.getId()).get();

		assertNotNull(output);
		assertNotNull(output.getId());

		assertEquals(expectedTitle, output.getTitle());
		assertEquals(expectedDescription, output.getDescription());
		assertEquals(expectedLaunchedAt, output.getLaunchedAt());
		assertEquals(expectedDuration, output.getDuration());
		assertEquals(expectedOpened, output.isOpened());
		assertEquals(expectedPublished, output.isPublished());
		assertEquals(expectedRating, output.getRating());
		assertEquals(expectedCategories, output.getCategories());
		assertEquals(expectedGenres, output.getGenres());
		assertEquals(expectedCastMembers, output.getCastMembers());
		assertEquals(expectedVideo, output.getVideo().get());
		assertEquals(expectedTrailer, output.getTrailer().get());
		assertEquals(expectedBanner, output.getBanner().get());
		assertEquals(thumbnail, output.getThumbnail().get());
		assertEquals(thumbnailHalf, output.getThumbnailHalf().get());
	}

	@Test
	public void givenInvalidVideo_whenCallsFindById_shouldReturnEmptyVideo() {

		final var expectedTitle = Fixture.title();
		final var expectedDescription = Fixture.description();
		final var expectedLaunchedAt = Year.of(Fixture.year());
		final var expectedDuration = Fixture.duration();
		final var expectedOpened = Fixture.bool();
		final var expectedPublished = Fixture.bool();
		final var expectedRating = Fixture.Videos.rating();
		final var expectedCategories = Set.<CategoryID>of();
		final var expectedGenres = Set.<GenreID>of();
		final var expectedCastMembers = Set.<CastMemberID>of();
		final var expectedVideo = AudioVideoMedia.with(UUID.randomUUID().toString(), "any name", "/raw-location", "/encoded-location", MediaStatus.COMPLETED);
		final var expectedTrailer = AudioVideoMedia.with(UUID.randomUUID().toString(), "any name", "/raw-location", "/encoded-location", MediaStatus.COMPLETED);
		final var expectedBanner = ImageMedia.with(UUID.randomUUID().toString(), "any name", "/location");
		final var thumbnail = ImageMedia.with(UUID.randomUUID().toString(), "any name", "/location");
		final var thumbnailHalf = ImageMedia.with(UUID.randomUUID().toString(), "any name", "/location");

		final var video = videoGateway.create(Video.newVideo(
			expectedTitle,
			expectedDescription,
			expectedLaunchedAt,
			expectedDuration,
			expectedRating,
			expectedOpened,
			expectedPublished,
			expectedCategories,
			expectedGenres,
			expectedCastMembers
		  )
		  .setVideo(expectedVideo)
		  .setTrailer(expectedTrailer)
		  .setBanner(expectedBanner)
		  .setThumbnail(thumbnail)
		  .setThumbnailHalf(thumbnailHalf));

		final var output = videoGateway.findById(VideoID.unique());

		assertTrue(output.isEmpty());
	}

	@Test
	public void givenEmptyParams_whenCallsFindAll_shouldReturnAllVideos() {
		mockVideos();

		final var expectedPage = 0;
		final var expectedPerPage = 10;
		final var expectedTotal = 4;
		final var expectedDirection = "asc";
		final var expectedSort = "title";
		final var expectedTerms = "";

		final var query = new VideoSearchQuery(
		  expectedPage,
		  expectedPerPage,
		  expectedTerms,
		  expectedSort,
		  expectedDirection,
		  Set.of(),
		  Set.of(),
		  Set.of()
		);
		final var output = videoGateway.findAll(query);

		assertEquals(expectedPage, output.currentPage());
		assertEquals(expectedPerPage, output.perPage());
		assertEquals(expectedTotal, output.total());
		assertEquals(expectedTotal, output.items().size());
	}

	@Test
	public void givenEmptyVideos_whenCallsFindAll_shouldReturnEmptyList() {
		final var expectedPage = 0;
		final var expectedPerPage = 10;
		final var expectedTotal = 0;
		final var expectedDirection = "asc";
		final var expectedSort = "title";
		final var expectedTerms = "";

		final var query = new VideoSearchQuery(
		  expectedPage,
		  expectedPerPage,
		  expectedTerms,
		  expectedSort,
		  expectedDirection,
		  Set.of(),
		  Set.of(),
		  Set.of()
		);
		final var output = videoGateway.findAll(query);

		assertEquals(expectedPage, output.currentPage());
		assertEquals(expectedPerPage, output.perPage());
		assertEquals(expectedTotal, output.total());
		assertEquals(expectedTotal, output.items().size());
	}

	@Test
	public void givenValidCategory_whenCallFindAll_shouldReturnFilteredList() {
		mockVideos();

		final var expectedPage = 0;
		final var expectedPerPage = 10;
		final var expectedTotal = 2;
		final var expectedDirection = "asc";
		final var expectedSort = "title";
		final var expectedTerms = "";

		final var query = new VideoSearchQuery(
		  expectedPage,
		  expectedPerPage,
		  expectedTerms,
		  expectedSort,
		  expectedDirection,
		  Set.of(CATEGORY.getId()),
		  Set.of(),
		  Set.of()
		);
		final var output = videoGateway.findAll(query);

		assertEquals(expectedPage, output.currentPage());
		assertEquals(expectedPerPage, output.perPage());
		assertEquals(expectedTotal, output.total());
		assertEquals(expectedTotal, output.items().size());
	}

	@Test
	public void givenValidCastMember_whenCallFindAll_shouldReturnFilteredList() {
		mockVideos();

		final var expectedPage = 0;
		final var expectedPerPage = 10;
		final var expectedTotal = 2;
		final var expectedDirection = "asc";
		final var expectedSort = "title";
		final var expectedTerms = "";

		final var query = new VideoSearchQuery(
		  expectedPage,
		  expectedPerPage,
		  expectedTerms,
		  expectedSort,
		  expectedDirection,
		  Set.of(),
		  Set.of(GENRE.getId()),
		  Set.of()
		);
		final var output = videoGateway.findAll(query);

		assertEquals(expectedPage, output.currentPage());
		assertEquals(expectedPerPage, output.perPage());
		assertEquals(expectedTotal, output.total());
		assertEquals(expectedTotal, output.items().size());
	}

	@Test
	public void givenValidGenre_whenCallFindAll_shouldReturnFilteredList() {
		mockVideos();

		final var expectedPage = 0;
		final var expectedPerPage = 10;
		final var expectedTotal = 2;
		final var expectedDirection = "asc";
		final var expectedSort = "title";
		final var expectedTerms = "";

		final var query = new VideoSearchQuery(
		  expectedPage,
		  expectedPerPage,
		  expectedTerms,
		  expectedSort,
		  expectedDirection,
		  Set.of(),
		  Set.of(),
		  Set.of(CAST_MEMBER.getId())
		);
		final var output = videoGateway.findAll(query);

		assertEquals(expectedPage, output.currentPage());
		assertEquals(expectedPerPage, output.perPage());
		assertEquals(expectedTotal, output.total());
		assertEquals(expectedTotal, output.items().size());
	}

	@Test
	public void givenAllParameters_whenCallFindAll_shouldReturnFilteredList() {
		mockVideos();

		final var expectedPage = 0;
		final var expectedPerPage = 10;
		final var expectedTotal = 1;
		final var expectedDirection = "asc";
		final var expectedSort = "title";
		final var expectedTerms = "a";

		final var query = new VideoSearchQuery(
		  expectedPage,
		  expectedPerPage,
		  expectedTerms,
		  expectedSort,
		  expectedDirection,
		  Set.of(CATEGORY.getId()),
		  Set.of(GENRE.getId()),
		  Set.of(CAST_MEMBER.getId())
		);
		final var output = videoGateway.findAll(query);

		assertEquals(expectedPage, output.currentPage());
		assertEquals(expectedPerPage, output.perPage());
		assertEquals(expectedTotal, output.total());
		assertEquals(expectedTotal, output.items().size());
	}

	@ParameterizedTest
	@CsvSource({
	  "mat,0,10,1,1,Matrix",
	  "tita,0,10,1,1,Titanic",
	  "harry,0,10,1,1,Harry Potter",
	  "vinga,0,10,1,1,Vingadores",
	})
	public void givenValidTerm_whenCallsFindAll_shouldReturnFiltered(
	  final String expectedTerms,
	  final int expectedPage,
	  final int expectedPerPage,
	  final int expectedItemsCount,
	  final long expectedTotal,
	  final String expectedVideo
	) {
		mockVideos();

		final var expectedSort = "title";
		final var expectedDirection = "asc";

		final var query = new VideoSearchQuery(
		  expectedPage,
		  expectedPerPage,
		  expectedTerms,
		  expectedSort,
		  expectedDirection,
		  Set.of(),
		  Set.of(),
		  Set.of()
		);
		final var output = videoGateway.findAll(query);

		assertEquals(expectedPage, output.currentPage());
		assertEquals(expectedPerPage, output.perPage());
		assertEquals(expectedTotal, output.total());
		assertEquals(expectedItemsCount, output.items().size());
		assertEquals(expectedVideo, output.items().get(0).title());
	}

	/*

		@CsvSource({
	  "matrix,0,10,1,1,Realidade virtual e luta pela liberdade da humanidade",
	  "titanic,0,10,1,1,Amor proibido e tragédia no maior navio do mundo",
	  "harry Potter,0,10,1,1,Jovem bruxo descobre seu destino e enfrenta o mal em Hogwarts",
	  "vingadores,0,10,1,1,Super-heróis unem forças para salvar o mundo de vilões poderosos",
	})
	 */

	@ParameterizedTest
	@CsvSource({
	  "title,asc,0,10,4,4,Harry Potter",
	  "title,desc,0,10,4,4,Vingadores",
	  "createdAt,asc,0,10,4,4,Matrix",
	})
	public void givenValidSortAndDirection_whenCallsFindAll_shouldReturnFiltered(
	  final String expectedSort ,
	  final String expectedDirection,
	  final int expectedPage,
	  final int expectedPerPage,
	  final int expectedItemsCount,
	  final long expectedTotal,
	  final String expectedVideo
	) {
		mockVideos();

		final var expectedTerms = "";

		final var query = new VideoSearchQuery(
		  expectedPage,
		  expectedPerPage,
		  expectedTerms,
		  expectedSort,
		  expectedDirection,
		  Set.of(),
		  Set.of(),
		  Set.of()
		);
		final var output = videoGateway.findAll(query);

		assertEquals(expectedPage, output.currentPage());
		assertEquals(expectedPerPage, output.perPage());
		assertEquals(expectedTotal, output.total());
		assertEquals(expectedItemsCount, output.items().size());
		assertEquals(expectedVideo, output.items().get(0).title());
	}

	@ParameterizedTest
	@CsvSource({
	  "0,2,2,4,Harry Potter;Matrix",
	  "1,2,2,4,Titanic;Vingadores",
	})
	public void givenValidSortAndDirection_whenCallsFindAll_shouldReturnFiltered(
	  final int expectedPage,
	  final int expectedPerPage,
	  final int expectedItemsCount,
	  final long expectedTotal,
	  final String expectedVideo
	) {
		mockVideos();

		final var expectedTerms = "";
		final var expectedSort = "title";
		final var expectedDirection = "asc";

		final var query = new VideoSearchQuery(
		  expectedPage,
		  expectedPerPage,
		  expectedTerms,
		  expectedSort,
		  expectedDirection,
		  Set.of(),
		  Set.of(),
		  Set.of()
		);
		final var output = videoGateway.findAll(query);

		assertEquals(expectedPage, output.currentPage());
		assertEquals(expectedPerPage, output.perPage());
		assertEquals(expectedTotal, output.total());
		assertEquals(expectedItemsCount, output.items().size());

		int index = 0;
		for (String expectedName: expectedVideo.split(";")) {
			final var name = output.items().get(index).title();
			assertEquals(expectedName, name);
			index++;
		}
	}

	private void mockVideos() {
		genreGateway.create(GENRE);
		categoryGateway.create(CATEGORY);
		castMemberGateway.create(CAST_MEMBER);

		videoGateway.create(Video.newVideo(
		  "Matrix",
		  "Realidade virtual e luta pela liberdade da humanidade",
		  Year.of(Fixture.year()),
		  Fixture.duration(),
		  Fixture.Videos.rating(),
		  Fixture.bool(),
		  Fixture.bool(),
		  Set.of(CATEGORY.getId()),
		  Set.of(),
		  Set.of()
		));

		videoGateway.create(Video.newVideo(
		  "Titanic",
		  "Amor proibido e tragédia no maior navio do mundo",
		  Year.of(Fixture.year()),
		  Fixture.duration(),
		  Fixture.Videos.rating(),
		  Fixture.bool(),
		  Fixture.bool(),
		  Set.of(),
		  Set.of(GENRE.getId()),
		  Set.of()
		));

		videoGateway.create(Video.newVideo(
		  "Harry Potter",
		  "Jovem bruxo descobre seu destino e enfrenta o mal em Hogwarts",
		  Year.of(Fixture.year()),
		  Fixture.duration(),
		  Fixture.Videos.rating(),
		  Fixture.bool(),
		  Fixture.bool(),
		  Set.of(),
		  Set.of(),
		  Set.of(CAST_MEMBER.getId())
		));

		videoGateway.create(Video.newVideo(
		  "Vingadores",
		  "Super-heróis unem forças para salvar o mundo de vilões poderosos",
		  Year.of(Fixture.year()),
		  Fixture.duration(),
		  Fixture.Videos.rating(),
		  Fixture.bool(),
		  Fixture.bool(),
		  Set.of(CATEGORY.getId()),
		  Set.of(GENRE.getId()),
		  Set.of(CAST_MEMBER.getId())
		));
	}

}

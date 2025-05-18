package com.fullcycle.admin.catalog.application.video.retrieve.list;

import com.fullcycle.admin.catalog.application.Fixture;
import com.fullcycle.admin.catalog.application.UseCaseTest;
import com.fullcycle.admin.catalog.domain.pagination.Pagination;
import com.fullcycle.admin.catalog.domain.pagination.VideoSearchQuery;
import com.fullcycle.admin.catalog.domain.video.Video;
import com.fullcycle.admin.catalog.domain.video.VideoGateway;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ListVideoUseCaseTest extends UseCaseTest {

	@Mock
	private VideoGateway videoGateway;

	@InjectMocks
	private DefaultListVideosUseCase useCase;

	@Override
	protected List<Object> getMocks() {
		return List.of(videoGateway);
	}

	@Test
	public void givenValidQuery_whenCallsListVideos_thenShouldReturnVideos() {
		final var videos = List.of(Fixture.Videos.video(), Fixture.Videos.video());

		final var expectedPage = 0;
		final var expectedPerPage = 10;
		final var expectedTerms = "any";
		final var expectedSort = "createdAt";
		final var expectedDirection = "asc";
		final var expectedTotal = 2;

		final var expectedItems = videos.stream()
		  .map(VideoListOutput::from)
		  .toList();

		final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, expectedTotal, videos);

		when(videoGateway.findAll(any())).thenReturn(expectedPagination);

		final var query = new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
		final var output = useCase.execute(query);

		assertEquals(expectedPage, output.currentPage());
		assertEquals(expectedPerPage, output.perPage());
		assertEquals(expectedTotal, output.total());
		assertEquals(expectedItems, output.items());
		verify(videoGateway, times(1)).findAll(query);
	}

	@Test
	public void givenValidQuery_whenCallsListVideoAndResultIsEmpty_shouldReturnGenres() {
		final var videos = List.<Video>of();

		final var expectedPage = 0;
		final var expectedPerPage = 10;
		final var expectedTerms = "any";
		final var expectedSort = "createdAt";
		final var expectedDirection = "asc";
		final var expectedTotal = 2;

		final var expectedItems = videos.stream()
		  .map(VideoListOutput::from)
		  .toList();

		final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, expectedTotal, videos);

		when(videoGateway.findAll(any())).thenReturn(expectedPagination);

		final var query = new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
		final var output = useCase.execute(query);

		assertEquals(expectedPage, output.currentPage());
		assertEquals(expectedPerPage, output.perPage());
		assertEquals(expectedTotal, output.total());
		assertEquals(expectedItems, output.items());
		verify(videoGateway, times(1)).findAll(query);
	}

	@Test
	public void givenValidQuery_whenCallsListVideoAndGatewayThrowsRandomError_shouldReturnException() {
		final var expectedPage = 0;
		final var expectedPerPage = 10;
		final var expectedTerms = "any";
		final var expectedSort = "createdAt";
		final var expectedDirection = "asc";
		final var expectedErrorMessage = "Gateway error";

		when(videoGateway.findAll(any())).thenThrow(new IllegalStateException(expectedErrorMessage));

		final var query = new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
		final var output = assertThrows(IllegalStateException.class, () -> useCase.execute(query));

		assertEquals(expectedErrorMessage, output.getMessage());
	}
}

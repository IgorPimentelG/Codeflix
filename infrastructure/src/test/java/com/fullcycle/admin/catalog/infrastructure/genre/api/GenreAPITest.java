package com.fullcycle.admin.catalog.infrastructure.genre.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcycle.admin.catalog.ControllerTest;
import com.fullcycle.admin.catalog.application.genre.create.CreateGenreOutput;
import com.fullcycle.admin.catalog.application.genre.create.CreateGenreUseCase;
import com.fullcycle.admin.catalog.application.genre.delete.DeleteGenreUseCase;
import com.fullcycle.admin.catalog.application.genre.retrieve.get.GenreOutput;
import com.fullcycle.admin.catalog.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.fullcycle.admin.catalog.application.genre.retrieve.list.GenreListOutput;
import com.fullcycle.admin.catalog.application.genre.retrieve.list.ListGenreUseCase;
import com.fullcycle.admin.catalog.application.genre.update.UpdateGenreCommand;
import com.fullcycle.admin.catalog.application.genre.update.UpdateGenreOutput;
import com.fullcycle.admin.catalog.application.genre.update.UpdateGenreUseCase;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.errors.NotFoundException;
import com.fullcycle.admin.catalog.domain.errors.NotificationException;
import com.fullcycle.admin.catalog.domain.genre.Genre;
import com.fullcycle.admin.catalog.domain.genre.GenreID;
import com.fullcycle.admin.catalog.domain.pagination.Pagination;
import com.fullcycle.admin.catalog.domain.validation.Error;
import com.fullcycle.admin.catalog.domain.validation.handler.Notification;
import com.fullcycle.admin.catalog.infrastructure.api.GenreAPI;
import com.fullcycle.admin.catalog.infrastructure.genre.models.CreateGenreRequest;
import com.fullcycle.admin.catalog.infrastructure.genre.models.UpdateGenreRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = GenreAPI.class)
public class GenreAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private CreateGenreUseCase createGenreUseCase;

    @MockitoBean
    private GetGenreByIdUseCase getGenreByIdUseCase;

    @MockitoBean
    private UpdateGenreUseCase updateGenreUseCase;

    @MockitoBean
    private DeleteGenreUseCase deleteGenreUseCase;

    @MockitoBean
    private ListGenreUseCase listGenreUseCase;

    @Test
    public void givenValidCommand_whenCallsCreateGenre_shouldReturnGenreId() throws Exception {
        final var expectedName = "any name";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        final var expectedId = GenreID.from(UUID.randomUUID());

        final var input = new CreateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        when(createGenreUseCase.execute(any())).thenReturn(CreateGenreOutput.from(expectedId));

        final var request = MockMvcRequestBuilders.post("/genres")
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .content(mapper.writeValueAsString(input));

        final var response = mvc.perform(request).andDo(print());

        response.andExpect(status().isCreated())
          .andExpect(header().string("Location", "/genres/" + expectedId.toString()))
          .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
          .andExpect(jsonPath("$.genre_id", equalTo(expectedId.toString())));

        verify(createGenreUseCase).execute(argThat(cmd ->
          Objects.equals(expectedName, cmd.name()) &&
          Objects.equals(expectedIsActive, cmd.isActive()) &&
          Objects.equals(expectedCategories, cmd.categories())
        ));
    }

    @Test
    public void givenInvalidName_whenCallsCreateGenre_shouldReturnNotification() throws Exception {
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.of(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        final var expectedErrorMessage = "Name should not be null";

        final var input = new CreateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        when(createGenreUseCase.execute(any())).thenThrow(new NotificationException(Notification.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/genres")
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .content(mapper.writeValueAsString(input));

        final var response = mvc.perform(request).andDo(print());

        response.andExpect(status().isUnprocessableEntity())
          .andExpect(header().string("Location", nullValue()))
          .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
          .andExpect(jsonPath("$.errors", hasSize(1)))
          .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(createGenreUseCase).execute(argThat(cmd ->
          Objects.equals(expectedName, cmd.name()) &&
            Objects.equals(expectedIsActive, cmd.isActive()) &&
            Objects.equals(expectedCategories, cmd.categories())
        ));
    }

    @Test
    public void givenValidId_whenCallsGetGenreById_shouldReturnGenre() throws Exception {
        final var expectedName = "any name";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(UUID.randomUUID(), UUID.randomUUID());

        final var genre = Genre.newGenre(expectedName, expectedIsActive);
        genre.addCategories(expectedCategories.stream().map(CategoryID::from).toList());
        final var expectedId = genre.getId();

        when(getGenreByIdUseCase.execute(any())).thenReturn(GenreOutput.from(genre));

        final var request = MockMvcRequestBuilders.get("/genres/{id}", expectedId.toString())
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON);

        final var response = mvc.perform(request).andDo(print());

        response.andExpect(status().isOk())
          .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
          .andExpect(jsonPath("$.id", equalTo(expectedId.toString())))
          .andExpect(jsonPath("$.name", equalTo(expectedName)))
          .andExpect(jsonPath("$.is_active", equalTo(expectedIsActive)))
          .andExpect(jsonPath("$.categories_id", equalTo(expectedCategories.stream().map(UUID::toString).toList())))
          .andExpect(jsonPath("$.created_at", equalTo(genre.getCreatedAt().toString())))
          .andExpect(jsonPath("$.updated_at", equalTo(genre.getUpdatedAt().toString())))
          .andExpect(jsonPath("$.deleted_at", equalTo(genre.getDeletedAt())));

        verify(getGenreByIdUseCase).execute(eq(expectedId.getValue()));
    }

    @Test
    public void givenInvalidId_whenCallsGetGenreById_shouldReturnNotFound() throws Exception {
        final var expectedId = UUID.randomUUID();
        final var expectedErrorMessage = "Genre with ID " + expectedId + " was not found";

        when(getGenreByIdUseCase.execute(any())).thenThrow(NotFoundException.with(Genre.class, GenreID.from(expectedId)));

        final var request = MockMvcRequestBuilders.get("/genres/{id}", expectedId.toString())
          .contentType(MediaType.APPLICATION_JSON);

        final var response = mvc.perform(request).andDo(print());

        response.andExpect(status().isNotFound())
          .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
          .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(getGenreByIdUseCase).execute(eq(expectedId));
    }

    @Test
    public void givenValidCommand_whenCallsUpdateGenre_shouldReturnGenreId() throws Exception {
        final var expectedName = "any name";
        final var expectedIsActive = true;
        final var expectedCategories = List.<String>of();

        final var genre = Genre.newGenre(expectedName, expectedIsActive);
        final var expectedId = genre.getId();

        when(updateGenreUseCase.execute(any())).thenReturn(UpdateGenreOutput.from(genre));

        final var input = new UpdateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        final var request = MockMvcRequestBuilders.put("/genres/{id}", expectedId.toString())
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .content(mapper.writeValueAsString(input));

        final var response = mvc.perform(request).andDo(print());

        response.andExpect(status().isOk())
          .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
          .andExpect(jsonPath("$.id", equalTo(expectedId.toString())));

        verify(updateGenreUseCase).execute(argThat(cmd ->
          Objects.equals(expectedName, cmd.name()) &&
            Objects.equals(expectedIsActive, cmd.isActive()) &&
            Objects.equals(expectedCategories, cmd.categories())
        ));
    }

    @Test
    public void givenValidId_whenCallsDeleteGenre_shouldBeOk() throws Exception {
        final var expectedId = UUID.randomUUID();

        doNothing().when(deleteGenreUseCase).execute(any());

        final var request = MockMvcRequestBuilders.delete("/genres/{id}", expectedId.toString())
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .accept(MediaType.APPLICATION_JSON_VALUE);

        final var response = mvc.perform(request).andDo(print());

        response.andExpect(status().isNoContent());
        verify(deleteGenreUseCase).execute(eq(expectedId));
    }

    @Test
    public void givenValidParams_whenCallsListGenre_shouldReturnGenres() throws Exception {
        final var genre = Genre.newGenre("any name", false);

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "ac";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(GenreListOutput.from(genre));

        when(listGenreUseCase.execute(any())).thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var request = MockMvcRequestBuilders.get("/genres")
          .queryParam("page", String.valueOf(expectedPage))
          .queryParam("perPage", String.valueOf(expectedPerPage))
          .queryParam("sort", expectedSort)
          .queryParam("dir", expectedDirection)
          .queryParam("search", expectedTerms)
          .accept(MediaType.APPLICATION_JSON);

        final var response = mvc.perform(request).andDo(print());

        response.andExpect(status().isOk())
          .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
          .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
          .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
          .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
          .andExpect(jsonPath("$.items[0].id", equalTo(genre.getId().toString())))
          .andExpect(jsonPath("$.items[0].name", equalTo(genre.getName())))
          .andExpect(jsonPath("$.items[0].is_active", equalTo(genre.isActive())))
          .andExpect(jsonPath("$.items[0].created_at", equalTo(genre.getCreatedAt().toString())))
          .andExpect(jsonPath("$.items[0].deleted_at", equalTo(genre.getDeletedAt().toString())));

        verify(listGenreUseCase).execute(argThat(cmd ->
          Objects.equals(expectedPage, cmd.page()) &&
          Objects.equals(expectedPerPage, cmd.perPage()) &&
          Objects.equals(expectedSort, cmd.sort()) &&
          Objects.equals(expectedDirection, cmd.direction()) &&
          Objects.equals(expectedTerms, cmd.terms())
        ));
    }
}

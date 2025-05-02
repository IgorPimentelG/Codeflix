package com.fullcycle.admin.catalog.infrastructure.castmember.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcycle.admin.catalog.ControllerTest;
import com.fullcycle.admin.catalog.Fixture;
import com.fullcycle.admin.catalog.application.castmember.create.CreateCastMemberOutput;
import com.fullcycle.admin.catalog.application.castmember.create.DefaultCreateCastMemberUseCase;
import com.fullcycle.admin.catalog.application.castmember.delete.DefaultDeleteCastMemberUseCase;
import com.fullcycle.admin.catalog.application.castmember.retrieve.get.CastMemberOutput;
import com.fullcycle.admin.catalog.application.castmember.retrieve.get.DefaultGetCastMemberUseCase;
import com.fullcycle.admin.catalog.application.castmember.retrieve.list.CastMemberListOutput;
import com.fullcycle.admin.catalog.application.castmember.retrieve.list.DefaultListCastMemberUseCase;
import com.fullcycle.admin.catalog.application.castmember.update.DefaultUpdateCastMemberUseCase;
import com.fullcycle.admin.catalog.application.castmember.update.UpdateCastMemberOutput;
import com.fullcycle.admin.catalog.domain.castmember.CastMember;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalog.domain.errors.NotFoundException;
import com.fullcycle.admin.catalog.domain.errors.NotificationException;
import com.fullcycle.admin.catalog.domain.pagination.Pagination;
import com.fullcycle.admin.catalog.domain.validation.Error;
import com.fullcycle.admin.catalog.domain.validation.handler.Notification;
import com.fullcycle.admin.catalog.infrastructure.api.CastMemberAPI;
import com.fullcycle.admin.catalog.infrastructure.castmember.models.CreateCastMemberRequest;
import com.fullcycle.admin.catalog.infrastructure.castmember.models.UpdateCastMemberRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ControllerTest(controllers = CastMemberAPI.class)
public class CastMemberAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private DefaultCreateCastMemberUseCase createCastMemberUseCase;

    @MockitoBean
    private DefaultUpdateCastMemberUseCase updateCastMemberUseCase;

    @MockitoBean
    private DefaultGetCastMemberUseCase getCastMemberByIdUseCase;

    @MockitoBean
    private DefaultListCastMemberUseCase listCastMemberUseCase;

    @MockitoBean
    private DefaultDeleteCastMemberUseCase deleteCastMemberUseCase;

    @Test
    public void givenValidCommand_whenCallsCreateCastMember_shouldReturnItsIdentifier() throws Exception {
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();
        final var expectedId = CastMemberID.unique();

        final var command = new CreateCastMemberRequest(expectedName, expectedType);

        when(createCastMemberUseCase.execute(any())).thenReturn(CreateCastMemberOutput.from(expectedId));

        final var request = MockMvcRequestBuilders.post("/cast-members")
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(command));

        mvc.perform(request)
          .andDo(print())
          .andExpect(status().isCreated())
          .andExpect(header().string("Location", "/cast-members/" + expectedId.getValue()))
          .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
          .andExpect(jsonPath("$.id", equalTo(expectedId.getValue().toString())));

        verify(createCastMemberUseCase).execute(argThat(cmd ->
            Objects.equals(expectedName, cmd.name()) &&
            Objects.equals(expectedType, cmd.type())
        ));
    }

    @Test
    public void givenInvalidName_whenCallsCreateCastMember_shouldReturnNotification() throws Exception {
        final String expectedName = null;
        final var expectedType = Fixture.CastMember.type();
        final var expectedErrorMessage = "Name cannot be null";

        final var command = new CreateCastMemberRequest(expectedName, expectedType);

        when(createCastMemberUseCase.execute(any()))
          .thenThrow(new NotificationException(Notification.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/cast-members")
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(command));

        mvc.perform(request)
          .andDo(print())
          .andExpect(status().isUnprocessableEntity())
          .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
          .andExpect(jsonPath("$.errors", hasSize(1)))
          .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(createCastMemberUseCase).execute(argThat(cmd ->
          Objects.equals(expectedName, cmd.name()) &&
          Objects.equals(expectedType, cmd.type())
        ));
    }

    @Test
    public void givenValidId_whenCallsGetById_shouldReturnIt() throws Exception {
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();

        final var castMember = CastMember.newMember(expectedName, expectedType);
        final var expectedId = castMember.getId();

        when(getCastMemberByIdUseCase.execute(any())).thenReturn(CastMemberOutput.from(castMember));

        final var request = MockMvcRequestBuilders.get("/cast-members/{id}", expectedId.getValue().toString())
          .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
          .andDo(print())
          .andExpect(status().isOk())
          .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
          .andExpect(jsonPath("$.id", equalTo(expectedId.getValue().toString())))
          .andExpect(jsonPath("$.name", equalTo(expectedName)))
          .andExpect(jsonPath("$.type", equalTo(expectedType.name())))
          .andExpect(jsonPath("$.created_at", equalTo(castMember.getCreatedAt().toString())));

        verify(getCastMemberByIdUseCase).execute(eq(expectedId.getValue()));
    }

    @Test
    public void givenInvalidId_whenCallsGetById_shouldReturnNotFound() throws Exception {
        final var expectedId = CastMemberID.unique();
        final var expectedErrorMessage = "CastMember with ID %s was not found".formatted(expectedId.getValue().toString());

        when(getCastMemberByIdUseCase.execute(any())).thenThrow(NotFoundException.with(CastMember.class, expectedId));

        final var request = MockMvcRequestBuilders.get("/cast-members/{id}", expectedId.getValue().toString())
          .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
          .andDo(print())
          .andExpect(status().isNotFound())
          .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
          .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(getCastMemberByIdUseCase).execute(eq(expectedId.getValue()));
    }

    @Test
    public void givenValidCommand_whenCallsUpdateCastMember_shouldReturnItsIdentifier() throws Exception {
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();
        final var castMember = CastMember.newMember(expectedName, expectedType);
        final var expectedId = castMember.getId();

        final var command = new UpdateCastMemberRequest(expectedName, expectedType);

        when(updateCastMemberUseCase.execute(any())).thenReturn(UpdateCastMemberOutput.from(expectedId));

        final var request = MockMvcRequestBuilders.put("/cast-members/{id}", expectedId.getValue().toString())
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(command));

        mvc.perform(request)
          .andDo(print())
          .andExpect(status().isOk())
          .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
          .andExpect(jsonPath("$.id", equalTo(expectedId.getValue().toString())));

        verify(updateCastMemberUseCase).execute(argThat(cmd ->
          Objects.equals(expectedId.getValue(), cmd.id()) &&
          Objects.equals(expectedName, cmd.name()) &&
          Objects.equals(expectedType, cmd.type())
        ));
    }

    @Test
    public void givenInvalidName_whenCallsUpdateCastMember_shouldReturnNotification() throws Exception {
        final String expectedName = null;
        final var expectedType = Fixture.CastMember.type();
        final var expectedId = CastMemberID.unique();
        final var expectedErrorMessage = "Name cannot be null";

        final var command = new UpdateCastMemberRequest(expectedName, expectedType);
        
        when(updateCastMemberUseCase.execute(any()))
          .thenThrow(new NotificationException(Notification.create(new Error(expectedErrorMessage))));


        final var request = MockMvcRequestBuilders.put("/cast-members/{id}", expectedId.getValue().toString())
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(command));

        mvc.perform(request)
          .andDo(print())
          .andExpect(status().isUnprocessableEntity())
          .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
          .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(updateCastMemberUseCase).execute(argThat(cmd ->
          Objects.equals(expectedId.getValue(), cmd.id()) &&
          Objects.equals(expectedName, cmd.name()) &&
          Objects.equals(expectedType, cmd.type())
        ));
    }

    @Test
    public void givenInvalidId_whenCallsUpdateCastMember_shouldReturnNotFound() throws Exception {
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();
        final var expectedId = CastMemberID.unique();
        final var expectedErrorMessage = "CastMember with ID %s was not found".formatted(expectedId.getValue().toString());

        final var command = new UpdateCastMemberRequest(expectedName, expectedType);

        when(updateCastMemberUseCase.execute(any())).thenThrow(NotFoundException.with(CastMember.class, expectedId));

        final var request = MockMvcRequestBuilders.put("/cast-members/{id}", expectedId.getValue().toString())
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(command));

        mvc.perform(request)
          .andDo(print())
          .andExpect(status().isNotFound())
          .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
          .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(updateCastMemberUseCase).execute(argThat(cmd ->
          Objects.equals(expectedId.getValue(), cmd.id()) &&
          Objects.equals(expectedName, cmd.name()) &&
          Objects.equals(expectedType, cmd.type())
        ));
    }

    @Test
    public void givenValidId_whenCallsDeleteCastMember_shouldDeleteIt() throws Exception {
        final var expectedId = CastMemberID.unique();

        doNothing().when(deleteCastMemberUseCase).execute(any());

        final var request = MockMvcRequestBuilders.delete("/cast-members/{id}", expectedId.getValue().toString())
          .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
          .andDo(print())
          .andExpect(status().isNoContent());

        verify(deleteCastMemberUseCase).execute(eq(expectedId.getValue()));
    }

    @Test
    public void givenValidParams_whenCallListCastMembers_shouldReturnIt() throws Exception {
        final var castMember = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());

        final var expectedPage = 1;
        final var expectedPerPage = 20;
        final var expectedTerms = "any term";
        final var expectedSort = "type";
        final var expectedDirection = "desc";

        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(CastMemberListOutput.from(castMember));

        when(listCastMemberUseCase.execute(any())).thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var request = MockMvcRequestBuilders.get("/cast-members")
          .queryParam("page", String.valueOf(expectedPage))
          .queryParam("perPage", String.valueOf(expectedPerPage))
          .queryParam("search", expectedTerms)
          .queryParam("sort", expectedSort)
          .queryParam("dir", expectedDirection)
          .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
          .andDo(print())
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
          .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
          .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
          .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
          .andExpect(jsonPath("$.items[0].id", equalTo(castMember.getId().getValue().toString())))
          .andExpect(jsonPath("$.items[0].name", equalTo(castMember.getName())))
          .andExpect(jsonPath("$.items[0].type", equalTo(castMember.getType().name())))
          .andExpect(jsonPath("$.items[0].created_at", equalTo(castMember.getCreatedAt().toString())));

        verify(listCastMemberUseCase).execute(argThat(query ->
          Objects.equals(expectedPage, query.page()) &&
          Objects.equals(expectedPerPage, query.perPage()) &&
          Objects.equals(expectedTerms, query.terms()) &&
          Objects.equals(expectedSort, query.sort()) &&
          Objects.equals(expectedDirection, query.direction())
        ));
    }


    @Test
    public void givenEmptyParams_whenCallListCastMembers_shouldUseDefaultsAndReturnIt() throws Exception {
        final var castMember = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());

        final var expectedPage = 0;
        final var expectedPerPage = 20;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "desc";

        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(CastMemberListOutput.from(castMember));

        when(listCastMemberUseCase.execute(any())).thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var request = MockMvcRequestBuilders.get("/cast-members")
          .queryParam("page", String.valueOf(expectedPage))
          .queryParam("perPage", String.valueOf(expectedPerPage))
          .queryParam("search", expectedTerms)
          .queryParam("sort", expectedSort)
          .queryParam("dir", expectedDirection)
          .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
          .andDo(print())
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
          .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
          .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
          .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
          .andExpect(jsonPath("$.items[0].id", equalTo(castMember.getId().getValue().toString())))
          .andExpect(jsonPath("$.items[0].name", equalTo(castMember.getName())))
          .andExpect(jsonPath("$.items[0].type", equalTo(castMember.getType().name())))
          .andExpect(jsonPath("$.items[0].created_at", equalTo(castMember.getCreatedAt().toString())));

        verify(listCastMemberUseCase).execute(argThat(query ->
          Objects.equals(expectedPage, query.page()) &&
            Objects.equals(expectedPerPage, query.perPage()) &&
            Objects.equals(expectedTerms, query.terms()) &&
            Objects.equals(expectedSort, query.sort()) &&
            Objects.equals(expectedDirection, query.direction())
        ));
    }
}

package com.fullcycle.admin.catalog.infrastructure.category.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcycle.admin.catalog.ControllerTest;
import com.fullcycle.admin.catalog.application.category.create.CreateCategoryOutput;
import com.fullcycle.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.fullcycle.admin.catalog.application.category.delete.DeleteCategoryUseCase;
import com.fullcycle.admin.catalog.application.category.retrieve.get.CategoryOutput;
import com.fullcycle.admin.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.fullcycle.admin.catalog.application.category.retrieve.list.CategoryListOutput;
import com.fullcycle.admin.catalog.application.category.retrieve.list.ListCategoriesUseCase;
import com.fullcycle.admin.catalog.application.category.update.UpdateCategoryOutput;
import com.fullcycle.admin.catalog.application.category.update.UpdateCategoryUseCase;
import com.fullcycle.admin.catalog.domain.category.Category;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.errors.DomainException;
import com.fullcycle.admin.catalog.domain.errors.NotFoundException;
import com.fullcycle.admin.catalog.domain.pagination.Pagination;
import com.fullcycle.admin.catalog.domain.validation.Error;
import com.fullcycle.admin.catalog.domain.validation.handler.Notification;
import com.fullcycle.admin.catalog.infrastructure.api.CategoryAPI;
import com.fullcycle.admin.catalog.infrastructure.category.models.CreateCategoryRequest;
import com.fullcycle.admin.catalog.infrastructure.category.models.UpdateCategoryRequest;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private CreateCategoryUseCase createCategoryUseCase;

    @MockitoBean
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    @MockitoBean
    private UpdateCategoryUseCase updateCategoryUseCase;

    @MockitoBean
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @MockitoBean
    private ListCategoriesUseCase listCategoriesUseCase;

    @Test
    public void givenValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() throws Exception {
        final var expectedName = "Any name";
        final var expectedDescription = "Any description";
        final var expectedIsActive = true;
        final var expectedId = UUID.randomUUID();

        final var input = new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        final var request = MockMvcRequestBuilders.post("/categories")
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(input));

        when(createCategoryUseCase.execute(any())).thenReturn(Either.right(CreateCategoryOutput.from(CategoryID.from(expectedId))));

        mvc.perform(request)
          .andDo(print())
          .andExpectAll(
            status().isCreated(),
            header().string("Location", String.format("/categories/%s", expectedId)),
            header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE)
          );

        verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
            Objects.equals(expectedName, cmd.name()) &&
            Objects.equals(expectedDescription, cmd.description()) &&
            Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenInvalidName_whenCallsCreateCategory_shouldReturnNotification() throws Exception {
        final String expectedName = null;
        final var expectedDescription = "Any description";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Name cannot be null";

        final var input = new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any())).thenReturn(Either.left(Notification.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/categories")
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(input));

        mvc.perform(request)
          .andDo(print())
          .andExpectAll(
            status().isUnprocessableEntity(),
            header().string("Location", nullValue()),
            header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
            jsonPath("$.errors", hasSize(1)),
            jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)
          ));
    }

    @Test
    public void givenInvalidCommand_whenCallsCreateCategory_shouldReturnDomainException() throws Exception {
        final String expectedName = null;
        final var expectedDescription = "Any description";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Name cannot be null";

        final var input = new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any())).thenThrow(DomainException.with(new Error(expectedErrorMessage)));

        final var request = MockMvcRequestBuilders.post("/categories")
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(input));

        mvc.perform(request)
          .andDo(print())
          .andExpectAll(
            status().isUnprocessableEntity(),
            header().string("Location", nullValue()),
            header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
            jsonPath("$.message", equalTo(expectedErrorMessage)),
            jsonPath("$.errors", hasSize(1)),
            jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)
          ));
    }

    @Test
    public void givenValidId_whenCallsGetCategory_shouldReturnCategory() throws Exception {
        final var expectedName = "Any name";
        final var expectedDescription = "Any description";
        final var expectedIsActive = true;

        Category category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var expectedId = category.getId().getValue();

        when(getCategoryByIdUseCase.execute(any())).thenReturn(CategoryOutput.from(category));

        final var request = MockMvcRequestBuilders.get("/categories/{id}", expectedId.toString()).contentType(MediaType.APPLICATION_JSON_VALUE);

        mvc.perform(request)
          .andDo(print())
          .andExpectAll(
            status().isOk(),
            jsonPath("$.id", equalTo(expectedId.toString())),
            jsonPath("$.name", equalTo(expectedName)),
            jsonPath("$.description", equalTo(expectedDescription)),
            jsonPath("$.is_active", equalTo(expectedIsActive)),
            jsonPath("$.created_at", equalTo(category.getCreatedAt().toString())),
            jsonPath("$.updated_at", equalTo(category.getUpdatedAt())),
            jsonPath("$.deleted_at", equalTo(category.getDeletedAt())
          ));

        verify(getCategoryByIdUseCase, times(1)).execute(eq(expectedId));
    }

    @Test
    public void givenInvValidId_whenCallsGetCategory_shouldReturnNotFoundException() throws Exception {
        final var expectedId = CategoryID.from(UUID.randomUUID());
        final var expectedErrorMessage = "Category with ID %s was not found".formatted(expectedId.getValue().toString());

        when(getCategoryByIdUseCase.execute(any())).thenThrow(NotFoundException.with(Category.class, expectedId));

        final var request = MockMvcRequestBuilders.get("/categories/{id}", expectedId.getValue().toString()).contentType(MediaType.APPLICATION_JSON_VALUE);

        mvc.perform(request)
          .andDo(print())
          .andExpectAll(
            status().isNotFound(),
            jsonPath("$.message", equalTo(expectedErrorMessage))
          );
    }

    @Test
    public void givenValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() throws Exception {
        final var expectedName = "Any name";
        final var expectedDescription = "Any description";
        final var expectedIsActive = true;

        Category category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var expectedId = category.getId().getValue();

        when(updateCategoryUseCase.execute(any())).thenReturn(Right(UpdateCategoryOutput.from(category)));

        final var command = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId.toString())
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .content(mapper.writeValueAsString(command));

        mvc.perform(request)
          .andDo(print())
          .andExpectAll(
            status().isOk(),
            jsonPath("$.category_id", equalTo(expectedId.toString())),
            header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE)
          );

        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd ->
            Objects.equals(expectedName, cmd.name()) &&
            Objects.equals(expectedDescription, cmd.description()) &&
            Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenCommandWithInvalidID_whenCallsUpdateCategory_thenShouldReturnNotFoundException() throws Exception {
        final var expectedName = "Any name";
        final var expectedDescription = "Any description";
        final var expectedIsActive = false;

        final var expectedId = CategoryID.from(UUID.randomUUID());
        final var expectedErrorMessage ="Category with ID %s was not found".formatted(expectedId.getValue());
        final var command = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        when(updateCategoryUseCase.execute(any())).thenThrow(NotFoundException.with(Category.class, expectedId));

        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId.getValue().toString())
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .content(mapper.writeValueAsString(command));

        mvc.perform(request)
          .andDo(print())
          .andExpectAll(
            status().isNotFound(),
            jsonPath("$.message", equalTo(expectedErrorMessage))
          );

        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd ->
          Objects.equals(expectedName, cmd.name()) &&
            Objects.equals(expectedDescription, cmd.description()) &&
            Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() throws Exception {
        final String expectedName = null;
        final var expectedDescription = "Any description";
        final var expectedIsActive = false;

        final var expectedId = CategoryID.from(UUID.randomUUID());
        final var expectedErrorMessage = "Name cannot be null";
        final var expectedErrorCount = 1;
        final var command = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        when(updateCategoryUseCase.execute(any())).thenReturn(Left(Notification.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId.getValue().toString())
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .content(mapper.writeValueAsString(command));

        mvc.perform(request)
          .andDo(print())
          .andExpectAll(
            status().isUnprocessableEntity(),
            jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)),
            jsonPath("$.errors", hasSize(expectedErrorCount))
          );

        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd ->
          Objects.equals(expectedName, cmd.name()) &&
            Objects.equals(expectedDescription, cmd.description()) &&
            Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenValidId_whenCallsDeleteCategory_shouldBeOk() throws Exception {
        final var expectedId = UUID.randomUUID();

        doNothing().when(deleteCategoryUseCase).execute(any());

        final var request = MockMvcRequestBuilders.delete("/categories/{id}", expectedId.toString()).contentType(MediaType.APPLICATION_JSON_VALUE);

        mvc.perform(request).andDo(print()).andExpectAll(status().isNoContent());

        verify(deleteCategoryUseCase, times(1)).execute(eq(expectedId));
    }

    @Test
    public void givenValidParams_whenCallsListCategories_shouldReturnCategoriesFiltered() throws Exception {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "any-terms";
        final var expectedSort = "name";
        final var expectedDirection = "desc";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;
        final var category = Category.newCategory("any name", "any description", true);
        final var expectedItems = List.of(CategoryListOutput.from(category));

        when(listCategoriesUseCase.execute(any())).thenReturn(new Pagination<>(
          expectedPage,
          expectedPerPage,
          expectedTotal,
          expectedItems
        ));

        final var request = MockMvcRequestBuilders.get("/categories")
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .queryParam("page", String.valueOf(expectedPage))
          .queryParam("perPage", String.valueOf(expectedPerPage))
          .queryParam("sort", expectedSort)
          .queryParam("dir", expectedDirection)
          .queryParam("search", expectedTerms);

        mvc.perform(request)
          .andDo(print())
          .andExpectAll(
            status().isOk(),
            jsonPath("$.current_page", equalTo(expectedPage)),
            jsonPath("$.per_page", equalTo(expectedPerPage)),
            jsonPath("$.total", equalTo(expectedTotal)),
            jsonPath("$.items", hasSize(expectedItemsCount)),
            jsonPath("$.items[0].id", equalTo(category.getId().getValue().toString())),
            jsonPath("$.items[0].name", equalTo(category.getName())),
            jsonPath("$.items[0].description", equalTo(category.getDescription())),
            jsonPath("$.items[0].is_active", equalTo(category.isActive())),
            jsonPath("$.items[0].created_at", equalTo(category.getCreatedAt().toString()))
          );

        verify(listCategoriesUseCase, times(1)).execute(argThat(query ->
            Objects.equals(expectedPage, query.page()) &&
            Objects.equals(expectedPerPage, query.perPage()) &&
            Objects.equals(expectedSort, query.sort()) &&
            Objects.equals(expectedDirection, query.direction()) &&
            Objects.equals(expectedTerms, query.terms())
        ));
    }
}

package com.fullcycle.admin.catalog.infrastructure.api;

import com.fullcycle.admin.catalog.domain.pagination.Pagination;
import com.fullcycle.admin.catalog.infrastructure.category.models.CategoryListResponse;
import com.fullcycle.admin.catalog.infrastructure.category.models.CategoryResponse;
import com.fullcycle.admin.catalog.infrastructure.category.models.CreateCategoryRequest;
import com.fullcycle.admin.catalog.infrastructure.category.models.UpdateCategoryRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/categories")
@Tag(name = "Categories")
@PreAuthorize("hasAnyRole('ROLE_CATALOG_ADMIN', 'ROLE_CATALOG_CATEGORIES')")
public interface CategoryAPI {

    @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new category")
    @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Created successfully"),
      @ApiResponse(responseCode = "422", description = "Unprocessable error"),
      @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> createCategory(@RequestBody @Valid CreateCategoryRequest input);

    @GetMapping
    @Operation(summary = "List all categories paginated")
    @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Listed successfully"),
      @ApiResponse(responseCode = "422", description = "A invalid parameter was received"),
      @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    Pagination<CategoryListResponse> listCategories(
      @RequestParam(name = "search", required = false, defaultValue = "") final String search,
      @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
      @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
      @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
      @RequestParam(name = "dir", required = false, defaultValue = "DESC") final String direction
    );

    @GetMapping(
      value = "/{id}",
      produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Get a category by it's identifier")
    @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
      @ApiResponse(responseCode = "404", description = "Category was not found"),
      @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    CategoryResponse getById(@PathVariable("id") UUID id);

    @PutMapping(
      value = "/{id}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update a category by it's identifier")
    @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Category updated successfully"),
      @ApiResponse(responseCode = "404", description = "Category was not found"),
      @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> update(@PathVariable("id") UUID id, @RequestBody @Valid UpdateCategoryRequest input);

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a category by it's identifier")
    @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Category deleted successfully"),
      @ApiResponse(responseCode = "404", description = "Category was not found"),
      @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    void delete(@PathVariable("id") UUID id);
}

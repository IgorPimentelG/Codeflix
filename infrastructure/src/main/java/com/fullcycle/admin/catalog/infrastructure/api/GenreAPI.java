package com.fullcycle.admin.catalog.infrastructure.api;

import com.fullcycle.admin.catalog.domain.pagination.Pagination;
import com.fullcycle.admin.catalog.infrastructure.genre.models.CreateGenreRequest;
import com.fullcycle.admin.catalog.infrastructure.genre.models.GenreListResponse;
import com.fullcycle.admin.catalog.infrastructure.genre.models.GenreResponse;
import com.fullcycle.admin.catalog.infrastructure.genre.models.UpdateGenreRequest;
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
@RequestMapping("/genres")
@Tag(name = "Genre")
@PreAuthorize("hasAnyRole('ROLE_CATALOG_ADMIN', 'ROLE_CATALOG_GENRES')")
public interface GenreAPI {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new genre")
    @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Created successfully"),
      @ApiResponse(responseCode = "422", description = "Unprocessable error"),
      @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> create(@RequestBody @Valid CreateGenreRequest input);

    @GetMapping
    @Operation(summary = "List all genres paginated")
    @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Listed successfully"),
      @ApiResponse(responseCode = "422", description = "A invalid parameter was received"),
      @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    Pagination<GenreListResponse> list(
      @RequestParam(name = "search", required = false, defaultValue = "") final String search,
      @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
      @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
      @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
      @RequestParam(name = "dir", required = false, defaultValue = "DESC") final String direction
    );

    @GetMapping(value  = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get a genre by it's identifier")
    @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Genre retrieved successfully"),
      @ApiResponse(responseCode = "404", description = "Genre was not found"),
      @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    GenreResponse getById(@PathVariable("id") UUID id);

    @PutMapping(
      value = "/{id}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update a genre by it's identifier")
    @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Genre updated successfully"),
      @ApiResponse(responseCode = "404", description = "Genre was not found"),
      @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> update(@PathVariable("id") UUID id, @RequestBody @Valid UpdateGenreRequest input);

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a genre by it's identifier")
    @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Genre deleted successfully"),
      @ApiResponse(responseCode = "404", description = "Genre was not found"),
      @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    void delete(@PathVariable("id") UUID id);
}

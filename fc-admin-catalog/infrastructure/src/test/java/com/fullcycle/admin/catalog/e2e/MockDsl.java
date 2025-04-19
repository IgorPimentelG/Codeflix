package com.fullcycle.admin.catalog.e2e;

import com.fullcycle.admin.catalog.domain.Identifier;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.genre.GenreID;
import com.fullcycle.admin.catalog.infrastructure.category.models.CategoryResponse;
import com.fullcycle.admin.catalog.infrastructure.category.models.CreateCategoryRequest;
import com.fullcycle.admin.catalog.infrastructure.configuration.json.Json;
import com.fullcycle.admin.catalog.infrastructure.genre.models.CreateGenreRequest;
import com.fullcycle.admin.catalog.infrastructure.genre.models.GenreResponse;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public interface MockDsl {

    MockMvc mvc();

    private String given(final String url, final Object body) throws Exception {
        final var request = MockMvcRequestBuilders.post(url)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .content(Json.writeValueAsString(body));

        final var id = this.mvc().perform(request)
          .andExpect(status().isCreated())
          .andReturn()
          .getResponse().getHeader("Location")
          .replace(url + "/", "");

        return id;
    }

    default CategoryID givenCategory(final String name, final String description, final boolean isActive) throws Exception {
        final var input = new CreateCategoryRequest(name, description, isActive);
        final var id = this.given("/categories", input);
        return CategoryID.from(id);
    }

    default GenreID givenGenre(final String name, final boolean isActive, final List<CategoryID> categories) throws Exception {
        final var categoriesMapped = mapTo(categories, (item) -> item.getValue().toString());
        final var input = new CreateGenreRequest(name, categoriesMapped, isActive);
        final var id = this.given("/genres", input);
        return GenreID.from(id);
    }

    default ResultActions deleteCategory(final Identifier id) throws Exception {
        return this.delete("/categories", id);
    }

    default ResultActions deleteGenre(final Identifier id) throws Exception {
        return this.delete("/genres", id);
    }

    default ResultActions listCategories(final int page, final int perPage) throws Exception {
        return listCategories(page, perPage, "", "", "");
    }

    default ResultActions listCategories(final int page, final int perPage, String search) throws Exception {
        return listCategories(page, perPage, "", "", search);
    }

    default ResultActions listCategories(
      final int page,
      final int perPage,
      final String dir,
      final String sort,
      final String search
    ) throws Exception {
      return this.list("/categories", page, perPage, dir, sort, search);
    }

    default ResultActions listGenres(final int page, final int perPage) throws Exception {
        return listGenres(page, perPage, "", "", "");
    }

    default ResultActions listGenres(final int page, final int perPage, String search) throws Exception {
        return listGenres(page, perPage, "", "", search);
    }

    default ResultActions listGenres(
      final int page,
      final int perPage,
      final String dir,
      final String sort,
      final String search
    ) throws Exception {
        return this.list("/genres", page, perPage, dir, sort, search);
    }

    default CategoryResponse retrieveCategory(final Identifier id) throws Exception {
        return this.retrieve("/categories", id, CategoryResponse.class);
    }

    default GenreResponse retrieveGenre(final Identifier id) throws Exception {
        return this.retrieve("/genres", id, GenreResponse.class);
    }

    default ResultActions updateCategory(final Identifier id, final Object body) throws Exception {
        return this.update("/categories", id, body);
    }

    default ResultActions updateGenre(final Identifier id, final Object body) throws Exception {
        return this.update("/genres", id, body);
    }

    private <T> T retrieve(final String url, final Identifier id, final Class<T> clazz) throws Exception {
        final var request = MockMvcRequestBuilders.get(url + "/" + id.getValue().toString()).contentType(MediaType.APPLICATION_JSON);
        final var json = this.mvc().perform(request)
          .andExpect(status().isOk())
          .andReturn()
          .getResponse().getContentAsString();
        return Json.readValue(json, clazz);
    }

    private ResultActions delete(final String url, final Identifier id) throws Exception {
        final var request = MockMvcRequestBuilders.delete(url + "/" + id.getValue().toString()).contentType(MediaType.APPLICATION_JSON);
        return this.mvc().perform(request);
    }

    private ResultActions update(final String url, final Identifier id, final Object body) throws Exception {
        final var request = MockMvcRequestBuilders.put(url + "/" + id.getValue().toString())
          .contentType(MediaType.APPLICATION_JSON)
          .content(Json.writeValueAsString(body));
        return this.mvc().perform(request);
    }

    private ResultActions list(
      final String url,
      final int page,
      final int perPage,
      final String dir,
      final String sort,
      final String search
    ) throws Exception {
        final var request = MockMvcRequestBuilders.get(url)
          .queryParam("page", String.valueOf(page))
          .queryParam("perPage", String.valueOf(perPage))
          .queryParam("dir", dir)
          .queryParam("sort", sort)
          .queryParam("search", search)
          .accept(MediaType.APPLICATION_JSON)
          .contentType(MediaType.APPLICATION_JSON);
        return this.mvc().perform(request);
    }

    default <A, D> List<D> mapTo(final List<A> actual, final Function<A, D> mapper) {
        return actual.stream().map(mapper).toList();
    }
}

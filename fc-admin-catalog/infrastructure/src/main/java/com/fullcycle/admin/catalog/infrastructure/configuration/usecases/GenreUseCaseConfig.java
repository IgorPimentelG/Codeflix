package com.fullcycle.admin.catalog.infrastructure.configuration.usecases;

import com.fullcycle.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.fullcycle.admin.catalog.application.category.create.DefaultCreateCategoryUseCase;
import com.fullcycle.admin.catalog.application.category.delete.DefaultDeleteCategoryUseCase;
import com.fullcycle.admin.catalog.application.category.delete.DeleteCategoryUseCase;
import com.fullcycle.admin.catalog.application.category.retrieve.get.DefaultGetCategoryByIdUseCase;
import com.fullcycle.admin.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.fullcycle.admin.catalog.application.category.retrieve.list.DefaultListCategoriesUseCase;
import com.fullcycle.admin.catalog.application.category.retrieve.list.ListCategoriesUseCase;
import com.fullcycle.admin.catalog.application.category.update.DefaultUpdateCategoryUseCase;
import com.fullcycle.admin.catalog.application.category.update.UpdateCategoryUseCase;
import com.fullcycle.admin.catalog.application.genre.create.CreateGenreUseCase;
import com.fullcycle.admin.catalog.application.genre.create.DefaultCreateGenreUseCase;
import com.fullcycle.admin.catalog.application.genre.delete.DefaultDeleteGenreUseCase;
import com.fullcycle.admin.catalog.application.genre.delete.DeleteGenreUseCase;
import com.fullcycle.admin.catalog.application.genre.retrieve.get.DefaultGetGenreByIdUseCase;
import com.fullcycle.admin.catalog.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.fullcycle.admin.catalog.application.genre.retrieve.list.DefaultListGenreUseCase;
import com.fullcycle.admin.catalog.application.genre.retrieve.list.ListGenreUseCase;
import com.fullcycle.admin.catalog.application.genre.update.DefaultUpdateGenreUseCase;
import com.fullcycle.admin.catalog.application.genre.update.UpdateGenreUseCase;
import com.fullcycle.admin.catalog.domain.category.CategoryGateway;
import com.fullcycle.admin.catalog.domain.genre.GenreGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class GenreUseCaseConfig {

    private final GenreGateway genreGateway;
    private final CategoryGateway categoryGateway;

    @Bean
    public CreateGenreUseCase createGenreUseCase() {
        return new DefaultCreateGenreUseCase(categoryGateway, genreGateway);
    }

    @Bean
    public UpdateGenreUseCase updateGenreUseCase() {
        return new DefaultUpdateGenreUseCase(categoryGateway, genreGateway);
    }

    @Bean
    public GetGenreByIdUseCase getGenreByIdUseCase() {
        return new DefaultGetGenreByIdUseCase(genreGateway);
    }

    @Bean
    public ListGenreUseCase listGenresUseCase() {
        return new DefaultListGenreUseCase(genreGateway);
    }

    @Bean
    public DeleteGenreUseCase deleteGenreByIdUseCase() {
        return new DefaultDeleteGenreUseCase(genreGateway);
    }
}

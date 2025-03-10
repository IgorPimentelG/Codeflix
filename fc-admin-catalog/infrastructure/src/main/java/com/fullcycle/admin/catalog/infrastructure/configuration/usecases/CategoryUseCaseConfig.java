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
import com.fullcycle.admin.catalog.domain.category.CategoryGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class CategoryUseCaseConfig {

    private final CategoryGateway categoryGateway;

    @Bean
    public CreateCategoryUseCase createCategoryUseCase() {
        return new DefaultCreateCategoryUseCase(categoryGateway);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase() {
        return new DefaultUpdateCategoryUseCase(categoryGateway);
    }

    @Bean
    public GetCategoryByIdUseCase getCategoryByIdUseCase() {
        return new DefaultGetCategoryByIdUseCase(categoryGateway);
    }

    @Bean
    public ListCategoriesUseCase listCategoriesUseCase() {
        return new DefaultListCategoriesUseCase(categoryGateway);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryByIdUseCase() {
        return new DefaultDeleteCategoryUseCase(categoryGateway);
    }
}

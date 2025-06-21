package com.fullcycle.admin.catalog.application.category.delete;

import com.fullcycle.admin.catalog.IntegrationTest;
import com.fullcycle.admin.catalog.domain.category.Category;
import com.fullcycle.admin.catalog.domain.category.CategoryGateway;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;

@IntegrationTest
public class DeleteCategoryUseCaseIT {

    @Autowired
    private DeleteCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @MockitoSpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenValidId_whenCallsDeleteCategory_shouldBeOk() {
        final var category = Category.newCategory("any-name", "any-description", true);
        final var expectedId = category.getId();

        save(category);

        assertEquals(1, categoryRepository.count());
        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenInvalidId_whenCallsDeleteCategory_shouldBeOk() {
        final var expectedId = CategoryID.unique();
        assertEquals(0, categoryRepository.count());
        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenValidId_whenGatewayThrowsException_shouldReturnException() {
        final var category = Category.newCategory("any-name", "any-description", true);
        final var expectedId = category.getId();
        final var expectedErrorMessage = "Gateway error";

        doThrow(new IllegalStateException(expectedErrorMessage)).when(categoryGateway).delete(eq(expectedId));
        assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));
    }

    private void save(final Category... category) {
        categoryRepository.saveAllAndFlush(
          Arrays.stream(category)
            .map(CategoryJpaEntity::from)
            .toList()
        );
    }
}

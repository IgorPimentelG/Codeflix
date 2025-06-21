package com.fullcycle.admin.catalog.application.category.retrieve.get;

import com.fullcycle.admin.catalog.IntegrationTest;
import com.fullcycle.admin.catalog.domain.category.Category;
import com.fullcycle.admin.catalog.domain.category.CategoryGateway;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.errors.DomainException;
import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@IntegrationTest
public class GetCategoryByIdIT {

    @Autowired
    private GetCategoryByIdUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @MockitoSpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenValidId_whenCallsGetCategory_shouldReturnCategory() {
        final var expectedName = "Any name";
        final var expectedDescription = "Any description";
        final var expectedIsActive = true;
        Category category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var expectedId = category.getId();

        save(category);
        final var output = useCase.execute(expectedId.getValue());

        assertEquals(expectedName, output.name());
        assertEquals(expectedDescription, output.description());
        assertEquals(expectedIsActive, output.isActive());
        assertNotNull(output.createdAt());
        assertNull(output.updatedAt());
        assertNull(output.deletedAt());
    }

    @Test
    public void givenInvValidId_whenCallsGetCategory_shouldReturnNotFoundException() {
        final var expectedId = CategoryID.from(UUID.randomUUID());
        final var expectedErrorMessage = "Category with ID %s was not found".formatted(expectedId.getValue());

        final var exception = assertThrows(DomainException.class, () -> useCase.execute(expectedId.getValue()));

        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    public void givenValidId_whenGatewayThrowsException_shouldReturnException() {
        final var expectedErrorMessage = "Gateway error";
        final var expectedId = CategoryID.from(UUID.randomUUID());

        doThrow(new IllegalStateException(expectedErrorMessage)).when(categoryGateway).findById(any());
        final var exception = assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    private void save(final Category... category) {
        categoryRepository.saveAllAndFlush(
          Arrays.stream(category)
            .map(CategoryJpaEntity::from)
            .toList()
        );
    }
}

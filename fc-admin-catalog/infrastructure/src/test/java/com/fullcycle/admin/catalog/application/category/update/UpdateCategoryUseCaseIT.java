package com.fullcycle.admin.catalog.application.category.update;

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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@IntegrationTest
public class UpdateCategoryUseCaseIT {

    @Autowired
    private UpdateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @MockitoSpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
        final var category = Category.newCategory("Any name", "Any description", true);
        final var expectedName = "Any name";
        final var expectedDescription = "Any description";
        final var expectedIsActive = true;
        final var expectedId = category.getId();

        save(category);
        final var command = UpdateCategoryCommand.with(expectedId, expectedName, expectedDescription, expectedIsActive);


        var output = useCase.execute(command).get();

        assertNotNull(output);
        assertNotNull(output.categoryID());

        final var categoryUpdated = categoryRepository.findById(output.categoryID().getValue()).get();

        assertNotNull(categoryUpdated.getId());
        assertEquals(expectedName, categoryUpdated.getName());
        assertEquals(expectedName, categoryUpdated.getName());
        assertEquals(expectedDescription, categoryUpdated.getDescription());
        assertEquals(expectedIsActive, categoryUpdated.isActive());
        assertNotNull(categoryUpdated.getCreatedAt());
        assertNotNull(categoryUpdated.getUpdatedAt());
        assertNull(categoryUpdated.getDeletedAt());
    }

    @Test
    public void givenInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() {
        final var category = Category.newCategory("Any name", null, true);
        final String expectedName = null;
        final var expectedDescription = "Any description";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Name cannot be null";
        final var expectedErrorCount = 1;
        final var expectedId = category.getId();

        save(category);
        final var command = UpdateCategoryCommand.with(expectedId, expectedName, expectedDescription, expectedIsActive);

        var notification = useCase.execute(command).getLeft();

        assertNotNull(notification);
        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidInactivateCommand_whenCallsUpdateCategory_thenShouldReturnInactiveCategoryId() {
        final var category = Category.newCategory("Any name", "Any description", true);

        assertTrue(category.isActive());
        assertNull(category.getDeletedAt());

        final String expectedName = "Any name";
        final var expectedDescription = "Any description";
        final var expectedIsActive = false;
        final var expectedId = category.getId();

        save(category);
        final var command = UpdateCategoryCommand.with(expectedId, expectedName, expectedDescription, expectedIsActive);

        var output = useCase.execute(command).get();

        assertNotNull(output);
        assertNotNull(output.categoryID());

        final var categoryUpdated = categoryRepository.findById(output.categoryID().getValue()).get();

        assertNotNull(categoryUpdated.getId());
        assertEquals(expectedName, categoryUpdated.getName());
        assertEquals(expectedName, categoryUpdated.getName());
        assertEquals(expectedDescription, categoryUpdated.getDescription());
        assertEquals(expectedIsActive, categoryUpdated.isActive());
        assertNotNull(categoryUpdated.getCreatedAt());
        assertNotNull(categoryUpdated.getUpdatedAt());
        assertNotNull(categoryUpdated.getDeletedAt());
    }

    @Test
    public void givenValidCommand_whenCallsUpdateCategory_thenShouldReturnException() {
        final var category = Category.newCategory("Any name", null, true);
        final String expectedName = "Any name";
        final var expectedDescription = "Any description";
        final var expectedIsActive = false;
        final var expectedId = category.getId();
        final var expectedErrorMessage = "Gateway error";
        final var expectedErrorCount = 1;

        save(category);
        final var command = UpdateCategoryCommand.with(expectedId, expectedName, expectedDescription, expectedIsActive);

        doThrow(new IllegalStateException(expectedErrorMessage)).when(categoryGateway).update(any());
        var notification = useCase.execute(command).getLeft();

        assertNotNull(notification);
        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());
    }

    @Test
    public void givenCommandWithInvalidID_whenCallsUpdateCategory_thenShouldReturnNotFoundException() {
        final String expectedName = "Any name";
        final var expectedDescription = "Any description";
        final var expectedIsActive = false;
        final var expectedId = CategoryID.from(UUID.randomUUID());
        final var expectedErrorMessage ="Category with ID %s was not found".formatted(expectedId.getValue());

        final var expectedErrorCount = 1;

        final var command = UpdateCategoryCommand.with(expectedId, expectedName, expectedDescription, expectedIsActive);
        final var notification = assertThrows(DomainException.class, () -> useCase.execute(command));

        assertNotNull(notification);
        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());
    }

    private void save(final Category... category) {
        categoryRepository.saveAllAndFlush(
          Arrays.stream(category)
            .map(CategoryJpaEntity::from)
            .toList()
        );
    }
}

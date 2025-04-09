package com.fullcycle.admin.catalog.application.category.update;

import com.fullcycle.admin.catalog.application.UseCaseTest;
import com.fullcycle.admin.catalog.domain.category.Category;
import com.fullcycle.admin.catalog.domain.category.CategoryGateway;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.errors.DomainException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class UpdateCategoryUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway);
    }

    @Test
    public void givenValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
        final var category = Category.newCategory("Any name", null, true);
        final var expectedName = "Any name";
        final var expectedDescription = "Any description";
        final var expectedIsActive = true;
        final var expectedId = category.getId();

        final var command = UpdateCategoryCommand.with(expectedId, expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(category.clone()));
        when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

        var output = useCase.execute(command).get();

        assertNotNull(output);
        assertNotNull(output.categoryID());

        verify(categoryGateway, times(1)).findById(eq(expectedId));
        verify(categoryGateway, times(1)).update(argThat(updatedCategory ->
          Objects.equals(expectedName, updatedCategory.getName())
            && Objects.equals(expectedDescription, updatedCategory.getDescription())
            && Objects.equals(expectedIsActive, updatedCategory.isActive())
            && Objects.nonNull(updatedCategory.getId())
            && Objects.nonNull(updatedCategory.getCreatedAt())
            && Objects.nonNull(updatedCategory.getUpdatedAt())
            && Objects.isNull(updatedCategory.getDeletedAt())
        ));
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

        final var command = UpdateCategoryCommand.with(expectedId, expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(category.clone()));

        var notification = useCase.execute(command).getLeft();

        assertNotNull(notification);
        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());

        verify(categoryGateway, times(0)).update(any());
    }

    @Test
    public void givenInvalidInactivateCommand_whenCallsUpdateCategory_thenShouldReturnInactiveCategoryId() {
        final var category = Category.newCategory("Any name", null, true);

        assertTrue(category.isActive());
        assertNull(category.getDeletedAt());

        final String expectedName = "Any name";
        final var expectedDescription = "Any description";
        final var expectedIsActive = false;
        final var expectedId = category.getId();

        final var command = UpdateCategoryCommand.with(expectedId, expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(category.clone()));
        when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

        var output = useCase.execute(command).get();

        assertNotNull(output);
        assertNotNull(output.categoryID());

        verify(categoryGateway, times(1)).findById(eq(expectedId));
        verify(categoryGateway, times(1)).update(argThat(updatedCategory ->
          Objects.equals(expectedName, updatedCategory.getName())
            && Objects.equals(expectedDescription, updatedCategory.getDescription())
            && Objects.equals(expectedIsActive, updatedCategory.isActive())
            && Objects.nonNull(updatedCategory.getId())
            && Objects.nonNull(updatedCategory.getCreatedAt())
            && Objects.nonNull(updatedCategory.getUpdatedAt())
            && Objects.nonNull(updatedCategory.getDeletedAt())
        ));
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

        final var command = UpdateCategoryCommand.with(expectedId, expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(category.clone()));
        when(categoryGateway.update(any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        var notification = useCase.execute(command).getLeft();

        assertNotNull(notification);
        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());
        verify(categoryGateway, times(1)).update(any());
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

        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.empty());

        final var notification = assertThrows(DomainException.class, () -> useCase.execute(command));

        assertNotNull(notification);
        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());
    }


}

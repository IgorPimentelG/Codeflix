package com.fullcycle.admin.catalog.application.category.retrieve.get;

import com.fullcycle.admin.catalog.domain.category.Category;
import com.fullcycle.admin.catalog.domain.category.CategoryGateway;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.errors.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetCategoryByIdUseCaseTest {

    @InjectMocks
    private DefaultGetCategoryByIdUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenValidId_whenCallsGetCategory_shouldReturnCategory() {
        final var expectedName = "Any name";
        final var expectedDescription = "Any description";
        final var expectedIsActive = true;
        Category category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var expectedId = category.getId();

        when(categoryGateway.findById(expectedId)).thenReturn(Optional.of(category.clone()));

        final var output = useCase.execute(expectedId.getValue());

        assertEquals(expectedName, output.name());
        assertEquals(expectedDescription, output.description());
        assertEquals(expectedIsActive, output.isActive());
        assertEquals(category.getCreatedAt(), output.createdAt());
        assertEquals(category.getUpdatedAt(), output.updatedAt());
        assertEquals(category.getDeletedAt(), output.deletedAt());
    }

    @Test
    public void givenInvValidId_whenCallsGetCategory_shouldReturnNotFoundException() {
        final var expectedId = CategoryID.from(UUID.randomUUID());
        final var expectedErrorMessage = "Category with ID %s was not found".formatted(expectedId.getValue());

        when(categoryGateway.findById(expectedId)).thenReturn(Optional.empty());

        final var exception = assertThrows(DomainException.class, () -> useCase.execute(expectedId.getValue()));

        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    public void givenValidId_whenGatewayThrowsException_shouldReturnException() {
        final var expectedErrorMessage = "Gateway error";
        final var expectedId = CategoryID.from(UUID.randomUUID());

        when(categoryGateway.findById(expectedId)).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var exception = assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        assertEquals(expectedErrorMessage, exception.getMessage());
    }

}

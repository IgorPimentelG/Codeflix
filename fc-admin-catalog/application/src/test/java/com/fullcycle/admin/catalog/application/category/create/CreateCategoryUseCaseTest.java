package com.fullcycle.admin.catalog.application.category.create;

import com.fullcycle.admin.catalog.domain.category.CategoryGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest {

    @InjectMocks
    private DefaultCreateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
        final var expectedName = "Any name";
        final var expectedDescription = "Any description";
        final var expectedIsActive = true;

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);
        when(categoryGateway.create(any())).thenAnswer(returnsFirstArg());
        final var output = useCase.execute(command).get();

        assertNotNull(output);
        assertNotNull(output.categoryID());
        verify(categoryGateway, times(1)).create(argThat(category ->
            Objects.equals(expectedName, category.getName())
                && Objects.equals(expectedDescription, category.getDescription())
                && Objects.equals(expectedIsActive, category.isActive())
                && Objects.nonNull(category.getId())
                && Objects.nonNull(category.getCreatedAt())
                && Objects.isNull(category.getUpdatedAt())
                && Objects.isNull(category.getDeletedAt())
        ));
    }

    @Test
    public void givenInValidName_whenCallsCreateCategory_thenShouldReturnDomainException() {
        final String expectedName = null;
        final var expectedDescription = "Any description";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Name cannot be null";
        final var expectedErrorCount = 1;

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);
        final var notification = useCase.execute(command).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstError().message());
        verify(categoryGateway, times(0)).create(any());
    }

    @Test
    public void givenValidCommandWithInactiveCategory_whenCallsCreateCategory_thenShouldReturnInactiveCategoryId() {
        final String expectedName = "Any name";
        final var expectedDescription = "Any description";
        final var expectedIsActive = false;

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);
        when(categoryGateway.create(any())).thenAnswer(returnsFirstArg());
        final var output = useCase.execute(command).get();

        assertNotNull(output);
        assertNotNull(output.categoryID());

        verify(categoryGateway, times(1)).create(argThat(category ->
          Objects.equals(expectedName, category.getName())
            && Objects.equals(expectedDescription, category.getDescription())
            && Objects.equals(expectedIsActive, category.isActive())
            && Objects.nonNull(category.getId())
            && Objects.nonNull(category.getCreatedAt())
            && Objects.isNull(category.getUpdatedAt())
            && Objects.nonNull(category.getDeletedAt())
        ));
    }

    @Test
    public void givenValidCommand_whenGatewayThrowsRandomException_shouldReturnException() {
        final var expectedName = "Any name";
        final var expectedDescription = "Any description";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Gateway Error";
        final var expectedErrorCount = 1;

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);
        when(categoryGateway.create(any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var notification = useCase.execute(command).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstError().message());
        verify(categoryGateway, times(1)).create(any());
    }
}
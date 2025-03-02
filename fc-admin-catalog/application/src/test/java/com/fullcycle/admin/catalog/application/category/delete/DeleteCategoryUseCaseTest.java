package com.fullcycle.admin.catalog.application.category.delete;

import com.fullcycle.admin.catalog.domain.category.Category;
import com.fullcycle.admin.catalog.domain.category.CategoryGateway;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteCategoryUseCaseTest {

    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenValidId_whenCallsDeleteCategory_shouldBeOk() {
        final var category = Category.newCategory("any-name", "any-description", true);
        final var expectedId = category.getId();

        doNothing().when(categoryGateway).delete(eq(expectedId));

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
    }

    @Test
    public void givenInvalidId_whenCallsDeleteCategory_shouldBeOk() {
        final var expectedId = CategoryID.unique();
        doNothing().when(categoryGateway).delete(eq(expectedId));
        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        verify(categoryGateway, times(1)).delete(eq(expectedId));
    }

    @Test
    public void givenValidId_whenGatewayThrowsException_shouldReturnException() {
        final var category = Category.newCategory("any-name", "any-description", true);
        final var expectedId = category.getId();
        final var expectedErrorMessage = "Gateway error";

        doThrow(new IllegalStateException(expectedErrorMessage)).when(categoryGateway).delete(eq(expectedId));
        assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));
    }
}
